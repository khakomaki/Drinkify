package com.example.drinkify.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.drinkify.core.models.Drink

@Database(entities = [Drink::class], version = 1, exportSchema = false)
abstract class DrinkDatabase : RoomDatabase() {
    abstract val dao: DrinkDao
}