package com.example.drinkify.model

import androidx.room.*
import kotlinx.coroutines.flow.Flow

interface DrinkDao {
    @Delete
    suspend fun deleteDrink(drink: Drink)

    @Upsert
    suspend fun upsertDrink(drink: Drink)

    @Query("SELECT * FROM drink_table ORDER BY name ASC")
    suspend fun getAllDrinksByName(): Flow<List<Drink>>

    @Query("SELECT * FROM drink_table ORDER BY amountInMl ASC")
    suspend fun getAllDrinksByAmount(): Flow<List<Drink>>

    @Query("SELECT * FROM drink_table ORDER BY alcoholPercentage ASC")
    suspend fun getAllDrinksByAlcoholPercentage(): Flow<List<Drink>>
}