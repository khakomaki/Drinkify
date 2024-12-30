package com.example.drinkify.bac

import com.example.drinkify.core.models.ConsumedDrinkAdvanced

object BACCalculator {
    fun calculateBAC (
        consumedDrinks: List<ConsumedDrinkAdvanced>,
        weightKg: Float,
        isMale: Boolean
    ): Double {
        val alcoholDistributionRatio = if (isMale) 0.68 else 0.55
        val weightGrams = weightKg * 1000
        val currentTime = System.currentTimeMillis()

        val totalAlcohol = consumedDrinks.sumOf { drink ->
            val timeElapsedHours = (currentTime - drink.timestamp) / (1000 * 60 * 60)
            val alcoholGrams = drink.drinkAmountInMl * (drink.drinkAlcoholPercentage / 100) * 0.789
            val bacForDrink = (alcoholGrams / (weightGrams * alcoholDistributionRatio)) * 100

            // reduce by metabolized alcohol
            (bacForDrink - (timeElapsedHours * 0.015)).coerceAtLeast(0.0)
        }
        return totalAlcohol
    }
}