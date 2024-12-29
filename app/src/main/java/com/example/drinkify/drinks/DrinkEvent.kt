package com.example.drinkify.drinks

import com.example.drinkify.core.models.Drink

sealed interface DrinkEvent {
    data object SaveDrink: DrinkEvent
    data object SaveEditedDrink: DrinkEvent
    data object ShowDialog: DrinkEvent
    data object HideDialog: DrinkEvent

    data class EditDrink(val drink: Drink): DrinkEvent
    data class DeleteDrink(val drink: Drink): DrinkEvent
    data class ShowDeleteConfirmation(val drink: Drink): DrinkEvent

    data class SetName(val name: String): DrinkEvent
    data class SetAmountMl(val amountMl: Int): DrinkEvent
    data class SetAlcoholPercentage(val alcoholPercentage: Float): DrinkEvent
    data class SortDrinks(val sortType: SortType): DrinkEvent
}