package com.example.drinkify.consumed_drinks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.drinkify.core.database.ConsumedDrinkDao
import com.example.drinkify.core.models.ConsumedDrink
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ConsumedDrinkViewModel(private val consumedDrinkDao: ConsumedDrinkDao): ViewModel() {
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
                // validation
                if (selectedDrink == null) return
                val consumedDrink = ConsumedDrink(
                    userId = 0, // TODO change when multi-user support is added
                    drinkId = selectedDrink.id,
                    timestamp = time
                )
                // upsert
                viewModelScope.launch {
                    consumedDrinkDao.upsertConsumedDrink(consumedDrink)
                }
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
            ConsumedDrinkEvent.HideDialog -> {
                _state.update { it.copy(
                    isDeletingConsumedDrink = false,
                    selectedConsumedDrink = null
                ) }
            }
        }
    }
}