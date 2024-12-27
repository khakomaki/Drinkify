package com.example.drinkify.drinks

import com.example.drinkify.core.models.Drink

data class DrinkState(
    val drinks: List<Drink> = emptyList(),
    val name: String = "",
    val amountInMl: Int = 0,
    val alcoholPercentage: Float = 0f,
    val isAddingDrink: Boolean = false,
    val sortType: SortType = SortType.NAME
)