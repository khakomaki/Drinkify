package com.example.drinkify.core.database

import androidx.room.*
import com.example.drinkify.core.models.ConsumedDrink
import com.example.drinkify.core.models.ConsumedDrinkAdvanced
import kotlinx.coroutines.flow.Flow

@Dao
interface ConsumedDrinkDao {
    @Upsert
    suspend fun upsertConsumedDrink(consumedDrink: ConsumedDrink)

    // all consumed drinks with drink information
    @Query("SELECT * FROM consumed_drink_table WHERE userId = :userId ORDER BY timestamp DESC")
    @Transaction
    fun getConsumedDrinksAdvanced(userId: Int): Flow<List<ConsumedDrinkAdvanced>>

    @Delete
    suspend fun deleteConsumedDrink(consumedDrink: ConsumedDrink)
}
