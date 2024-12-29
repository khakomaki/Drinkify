package com.example.drinkify.drinks

import com.example.drinkify.core.models.Drink
import com.example.drinkify.core.models.User

data class DrinkState(
    val drinks: List<Drink> = emptyList(),
    val name: String = "",
    val amountInMl: Int = 0,
    val alcoholPercentage: Float = 0f,
    val isAddingDrink: Boolean = false,
    val isDeletingDrink: Boolean = false,
    val isEditingDrink: Boolean = false,
    val selectedDrink: Drink? = null,
    val sortType: SortType = SortType.NAME,
    val BAC: Float = 0f,
    val user: User? = null
)
