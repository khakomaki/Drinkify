package com.example.drinkify.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.drinkify.core.models.Drink
import com.example.drinkify.core.models.User

@Database(entities = [Drink::class, User::class], version = 3, exportSchema = false)
abstract class DrinkDatabase : RoomDatabase() {
    abstract val drinkDao: DrinkDao
    abstract val userDao: UserDao
}