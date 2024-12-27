package com.example.drinkify.core.database

import androidx.room.*
import com.example.drinkify.core.models.Drink
import kotlinx.coroutines.flow.Flow

@Dao
interface DrinkDao {
    @Delete
    suspend fun deleteDrink(drink: Drink)

    @Upsert
    suspend fun upsertDrink(drink: Drink)

    @Query("SELECT * FROM drink_table ORDER BY name ASC")
    fun getAllDrinksByName(): Flow<List<Drink>>

    @Query("SELECT * FROM drink_table ORDER BY amountInMl ASC")
    fun getAllDrinksByAmount(): Flow<List<Drink>>

    @Query("SELECT * FROM drink_table ORDER BY alcoholPercentage ASC")
    fun getAllDrinksByAlcoholPercentage(): Flow<List<Drink>>
}