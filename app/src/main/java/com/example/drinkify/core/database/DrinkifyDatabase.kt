package com.example.drinkify.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.drinkify.core.models.ConsumedDrink
import com.example.drinkify.core.models.Drink
import com.example.drinkify.core.models.DrinkSession
import com.example.drinkify.core.models.User

@Database(
    entities = [ConsumedDrink::class, Drink::class, DrinkSession::class, User::class],
    version = 10,
    exportSchema = false
)
abstract class DrinkifyDatabase : RoomDatabase() {
    abstract val consumedDrinkDao: ConsumedDrinkDao
    abstract val drinkDao: DrinkDao
    abstract val drinkSessionDao: DrinkSessionDao
    abstract val userDao: UserDao
}
