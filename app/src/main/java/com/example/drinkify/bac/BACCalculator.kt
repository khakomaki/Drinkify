package com.example.drinkify.bac

import com.example.drinkify.core.models.ConsumedDrinkAdvanced
import com.example.drinkify.core.models.Drink

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
        val weightGrams = weightKg * 1000.0
        val alcoholDistributionRatio = if (isMale) {
            VOL_DISTRIBUTION_MALE
        } else {
            VOL_DISTRIBUTION_FEMALE
        }

        // effect on BAC by every consumed drink
        val totalBAC = consumedDrinks.sumOf { consumedDrinkAdvanced ->
            val drink = consumedDrinkAdvanced.drink
            val consumedDrink = consumedDrinkAdvanced.consumedDrink

            // skip drinks in the future
            if (currentTime < consumedDrink.timestamp) {
                return@sumOf 0.0
            }

            val timeElapsedHours = (currentTime - consumedDrink.timestamp) / (HOUR_IN_MS)
            val alcoholGrams = drink.amountInMl * (drink.alcoholPercentage / 100.0) * ALCOHOL_DENSITY
            val bacForDrink = (alcoholGrams / (weightGrams * alcoholDistributionRatio)) * 100.0

            // reduce by metabolized alcohol
            (bacForDrink - (timeElapsedHours * METABOLISM_RATE)).coerceAtLeast(0.0)
        }
        return totalBAC
    }

    fun effectLast(
        drink: Drink,
        consumptionTime: Long,
        weightKg: Float,
        isMale: Boolean
    ): Long {
        val weightGrams = weightKg * 1000.0
        val alcoholDistributionRatio = if (isMale) {
            VOL_DISTRIBUTION_MALE
        } else {
            VOL_DISTRIBUTION_FEMALE
        }

        // calculate alcohol to be metabolized
        val alcoholGrams = drink.amountInMl * (drink.alcoholPercentage / 100.0) * ALCOHOL_DENSITY
        val bacForDrink = (alcoholGrams / (weightGrams * alcoholDistributionRatio)) * 100.0

        // calculate time to metabolize
        val timeToMetabolizeHours = bacForDrink / METABOLISM_RATE

        // add effect time to consumption time
        val effectEnd = consumptionTime + (timeToMetabolizeHours * HOUR_IN_MS).toLong()
        return effectEnd
    }
}
