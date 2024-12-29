package com.example.drinkify.core.database

import androidx.room.*
import com.example.drinkify.core.models.User

@Dao
interface UserDao {
    @Query("SELECT * FROM user_table LIMIT 1")
    suspend fun getUser(): User?

    @Upsert
    suspend fun upsertUser(user: User)
}