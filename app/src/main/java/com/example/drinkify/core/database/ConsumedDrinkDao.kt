package com.example.drinkify.core.database

import androidx.room.*
import com.example.drinkify.core.models.ConsumedDrink
import kotlinx.coroutines.flow.Flow

@Dao
interface ConsumedDrinkDao {
    @Upsert
    suspend fun upsertConsumedDrink(consumedDrink: ConsumedDrink)

    @Query("SELECT * FROM consumed_drink_table WHERE userId = :userId ORDER BY timestamp DESC")
    fun getConsumedDrinksByUser(userId: Int): Flow<List<ConsumedDrink>>

    @Delete
    suspend fun deleteConsumedDrink(consumedDrink: ConsumedDrink)
}