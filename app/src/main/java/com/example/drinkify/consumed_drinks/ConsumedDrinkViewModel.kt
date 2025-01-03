package com.example.drinkify.consumed_drinks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.drinkify.core.database.ConsumedDrinkDao
import com.example.drinkify.core.models.ConsumedDrink
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
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

            // saving consumed drink
            is ConsumedDrinkEvent.SaveConsumedDrink -> {
                val selectedDrink = _state.value.selectedDrink
                val time = _state.value.drinkTime
                // validation
                if (selectedDrink == null) return
                val consumedDrink = ConsumedDrink(
                    userId = 0,
                    drinkId = selectedDrink.id,
                    timestamp = time
                )
                // upsert
                viewModelScope.launch {
                    consumedDrinkDao.upsertConsumedDrink(consumedDrink)
                }
            }

            // deleting consumed drink
            is ConsumedDrinkEvent.DeleteConsumedDrink -> {
                viewModelScope.launch {
                    consumedDrinkDao.deleteConsumedDrink(event.consumedDrink)
                }
            }
        }
    }
}