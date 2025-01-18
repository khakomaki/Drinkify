package com.example.drinkify.consumed_drinks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.drinkify.bac.BACCalculator
import com.example.drinkify.core.database.ConsumedDrinkDao
import com.example.drinkify.core.database.DrinkSessionDao
import com.example.drinkify.core.models.ConsumedDrink
import com.example.drinkify.core.models.DrinkSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ConsumedDrinkViewModel(
    private val consumedDrinkDao: ConsumedDrinkDao,
    private val drinkSessionDao: DrinkSessionDao
): ViewModel() {
    private val _state = MutableStateFlow(ConsumedDrinkState())
    val state = _state.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        ConsumedDrinkState()
    )

    init {
        viewModelScope.launch {
            consumedDrinkDao.getConsumedDrinksAdvanced(userId = 0).collect { drinks ->
                _state.value = _state.value.copy(consumedDrinksAdvanced = drinks)
            }
        }
    }

    fun onEvent(event: ConsumedDrinkEvent) {
        when(event) {
            // setting time of consumption
            is ConsumedDrinkEvent.SetConsumptionTime -> {
                _state.value = _state.value.copy(drinkTime = event.timestamp)
            }

            // selecting drink for consumption
            is ConsumedDrinkEvent.SelectDrink -> {
                _state.value = _state.value.copy(selectedDrink = event.selectedDrink)
            }

            // selecting consumed drink
            is ConsumedDrinkEvent.SelectConsumedDrink -> {
                _state.value = _state.value.copy(selectedConsumedDrink = event.consumedDrink)
            }

            // saving consumed drink
            is ConsumedDrinkEvent.SaveConsumedDrink -> {
                val selectedDrink = _state.value.selectedDrink
                val time = _state.value.drinkTime
                val userId = 0 // TODO change when multi-user support is added
                // validation
                if (selectedDrink == null) return
                // session selection
                val effectEnd = BACCalculator.effectLast(
                    drink = selectedDrink,
                    consumptionTime = time,
                    weightKg = 80f, // TODO get correct weight
                    isMale = true   // TODO get correct gender
                )
                viewModelScope.launch {
                    val session = drinkSessionDao.getDrinkSession(
                        userId = userId,
                        effectStartTime = time,
                        effectEndTime = effectEnd
                    )
                    val sessionId: Int
                    if (session == null) {
                        val newDrinkSession = createDrinkSession(
                            userId = userId,
                            startTimestamp = time,
                            endTimestamp = effectEnd
                        )
                        sessionId = newDrinkSession.id
                    } else {
                        sessionId = session.id
                    }

                    val consumedDrink = ConsumedDrink(
                        userId = userId,
                        drinkId = selectedDrink.id,
                        timestamp = time,
                        sessionId = sessionId
                    )
                    // upsert
                    consumedDrinkDao.upsertConsumedDrink(consumedDrink)
                }
                // update session start or end if necessary
            }

            // showing deletion confirmation for consumed drink
            is ConsumedDrinkEvent.ShowDeleteConfirmation -> {
                _state.update { it.copy(
                    isDeletingConsumedDrink = true,
                    selectedConsumedDrink = event.consumedDrink
                ) }
            }

            // deleting consumed drink
            is ConsumedDrinkEvent.DeleteConsumedDrink -> {
                viewModelScope.launch {
                    consumedDrinkDao.deleteConsumedDrink(event.consumedDrink)
                }
            }

            // hiding dialog
            is ConsumedDrinkEvent.HideDialog -> {
                _state.update { it.copy(
                    isDeletingConsumedDrink = false,
                    selectedConsumedDrink = null
                ) }
            }

            // showing time picker
            is ConsumedDrinkEvent.ShowTimePicker -> {
                _state.update { it.copy(isPickingTime = true) }
            }

            // hiding time picker
            is ConsumedDrinkEvent.HideTimePicker -> {
                _state.update { it.copy(isPickingTime = false) }
            }
        }
    }

    private suspend fun createDrinkSession(userId: Int, startTimestamp: Long, endTimestamp: Long): DrinkSession {
        val newDrinkSession = DrinkSession(
            userId = userId,
            startTimestamp = startTimestamp,
            endTimeStamp = endTimestamp
        )
        val sessionId = drinkSessionDao.upsertDrinkSession(newDrinkSession)
        val insertedDrinkSession = drinkSessionDao.getDrinkSessionById(sessionId)
        return insertedDrinkSession ?: newDrinkSession
    }
}
