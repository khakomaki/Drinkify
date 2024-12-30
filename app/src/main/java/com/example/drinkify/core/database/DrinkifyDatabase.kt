package com.example.drinkify.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.drinkify.core.models.ConsumedDrink
import com.example.drinkify.core.models.Drink
import com.example.drinkify.core.models.User

@Database(entities = [ConsumedDrink::class, Drink::class, User::class], version = 4, exportSchema = false)
abstract class DrinkifyDatabase : RoomDatabase() {
    abstract val consumedDrinkDao: ConsumedDrinkDao
    abstract val drinkDao: DrinkDao
    abstract val userDao: UserDao
}