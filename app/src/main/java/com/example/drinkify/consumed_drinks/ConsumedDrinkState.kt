package com.example.drinkify.consumed_drinks

import com.example.drinkify.core.models.ConsumedDrink
import com.example.drinkify.core.models.ConsumedDrinkAdvanced
import com.example.drinkify.core.models.Drink

data class ConsumedDrinkState(
    val consumedDrinks: List<ConsumedDrink> = emptyList(),
    val consumedDrinksAdvanced: List<ConsumedDrinkAdvanced> = emptyList(),
    val selectedDrink: Drink? = null,
    val drinkTime: Long = 0
)
