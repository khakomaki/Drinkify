package com.example.drinkify.consumed_drinks

import com.example.drinkify.core.models.ConsumedDrink
import com.example.drinkify.core.models.Drink

sealed interface ConsumedDrinkEvent {
    data object SaveConsumedDrink: ConsumedDrinkEvent

    data class SelectDrink(val selectedDrink: Drink): ConsumedDrinkEvent
    data class SetConsumptionTime(val timestamp: Long): ConsumedDrinkEvent
    data class DeleteConsumedDrink(val consumedDrink: ConsumedDrink): ConsumedDrinkEvent
}