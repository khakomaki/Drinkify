package com.example.drinkify.stats

import com.example.drinkify.core.models.Drink

data class StatsState(
    val totalDrinkCount: Int = 0,
    val totalConsumedAlcohol: Double = 0.0,
    val mostConsumedDrink: Drink? = null
)
