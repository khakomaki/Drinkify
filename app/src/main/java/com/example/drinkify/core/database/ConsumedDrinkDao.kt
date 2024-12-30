package com.example.drinkify.core.database

import androidx.room.*
import com.example.drinkify.core.models.ConsumedDrink
import com.example.drinkify.core.models.ConsumedDrinkAdvanced
import kotlinx.coroutines.flow.Flow

@Dao
interface ConsumedDrinkDao {
    @Upsert
    suspend fun upsertConsumedDrink(consumedDrink: ConsumedDrink)

    // all consumed drinks
    @Query("SELECT * FROM consumed_drink_table WHERE userId = :userId ORDER BY timestamp DESC")
    fun getConsumedDrinksByUser(userId: Int): Flow<List<ConsumedDrink>>

    // all consumed drinks with drink information
    @Query("""
        SELECT
            cdt.id AS id,
            cdt.userId AS userId,
            cdt.timestamp AS timestamp,
            cdt.drinkId AS drinkId,
            dt.name AS drinkName,
            dt.amountInMl AS drinkAmountInMl,
            dt.alcoholPercentage AS drinkAlcoholPercentage
        FROM consumed_drink_table AS cdt
        INNER JOIN drink_table AS dt ON cdt.drinkId = dt.id
        WHERE cdt.userId = :userId
        ORDER BY cdt.timestamp DESC
    """)
    fun getConsumedDrinksAdvanced(userId: Int): Flow<List<ConsumedDrinkAdvanced>>

    @Delete
    suspend fun deleteConsumedDrink(consumedDrink: ConsumedDrink)
}