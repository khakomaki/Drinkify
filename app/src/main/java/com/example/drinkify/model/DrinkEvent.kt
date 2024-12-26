package com.example.drinkify.model

sealed interface DrinkEvent {
    object saveDrink: DrinkEvent
    data class setName(val name: String): DrinkEvent
    data class setAmountMl(val amountMl: Int): DrinkEvent
    data class setAlcoholPercentage(val alcoholPercentage: Float): DrinkEvent

    object showDialog: DrinkEvent
    object hideDialog: DrinkEvent

    data class sortDrinks(val sortType: SortType): DrinkEvent
    data class deleteDrink(val drink: Drink): DrinkEvent
}