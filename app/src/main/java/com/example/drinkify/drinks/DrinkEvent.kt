package com.example.drinkify.drinks

import com.example.drinkify.core.models.Drink

sealed interface DrinkEvent {
    object saveDrink: DrinkEvent
    object saveEditedDrink: DrinkEvent
    data class setName(val name: String): DrinkEvent
    data class setAmountMl(val amountMl: Int): DrinkEvent
    data class setAlcoholPercentage(val alcoholPercentage: Float): DrinkEvent

    object showDialog: DrinkEvent
    object hideDialog: DrinkEvent

    data class sortDrinks(val sortType: SortType): DrinkEvent
    data class deleteDrink(val drink: Drink): DrinkEvent
    data class editDrink(val drink: Drink): DrinkEvent
    data class showDeleteConfirmation(val drink: Drink): DrinkEvent
}