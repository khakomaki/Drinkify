package com.example.drinkify.bac

import com.example.drinkify.core.models.ConsumedDrinkAdvanced

object BACCalculator {
    // avoid using integers to ensure precision
    private const val ALCOHOL_DENSITY = 0.78945
    private const val HOUR_IN_MS = 1000.0 * 60.0 * 60.0
    private const val METABOLISM_RATE = 0.015
    private const val VOL_DISTRIBUTION_MALE = 0.71
    private const val VOL_DISTRIBUTION_FEMALE = 0.58

    fun calculateBAC (
        consumedDrinks: List<ConsumedDrinkAdvanced>,
        weightKg: Float,
        isMale: Boolean
    ): Double {
        val currentTime = System.currentTimeMillis()
        val weightGrams = weightKg * 1000
        val alcoholDistributionRatio = if (isMale) {
            VOL_DISTRIBUTION_MALE
        } else {
            VOL_DISTRIBUTION_FEMALE
        }

        // effect on BAC by every consumed drink
        val totalAlcohol = consumedDrinks.sumOf { drink ->
            val timeElapsedHours = (currentTime - drink.timestamp) / (HOUR_IN_MS)
            val alcoholGrams = drink.drinkAmountInMl * (drink.drinkAlcoholPercentage / 100) * ALCOHOL_DENSITY
            val bacForDrink = (alcoholGrams / (weightGrams * alcoholDistributionRatio)) * 100

            // reduce by metabolized alcohol
            (bacForDrink - (timeElapsedHours * METABOLISM_RATE)).coerceAtLeast(0.0)
        }
        return totalAlcohol
    }
}