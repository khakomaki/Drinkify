package com.example.drinkify.core.database

import androidx.room.*
import com.example.drinkify.core.models.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM user_table LIMIT 1")
    suspend fun getUser(): User?

    @Query("SELECT * FROM user_table LIMIT 1")
    fun getUserFlow(): Flow<User>

    @Upsert
    suspend fun upsertUser(user: User)
}
