package com.example.drinkify.core.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.drinkify.core.models.DrinkSession
import kotlinx.coroutines.flow.Flow

@Dao
interface DrinkSessionDao {
    @Upsert
    suspend fun upsertDrinkSession(drinkSession: DrinkSession): Long

    @Query("SELECT * FROM drink_session_table WHERE id = :id")
    suspend fun getDrinkSessionById(id: Long): DrinkSession?

    // select drink session for user by time range
    @Query("""
        SELECT *
        FROM drink_session_table
        WHERE userId = :userId
        AND (
            :effectStartTime BETWEEN startTimestamp AND endTimeStamp
            OR :effectEndTime BETWEEN startTimestamp AND endTimeStamp
        )
    """)
    suspend fun getDrinkSession(
        userId: Int,
        effectStartTime: Long,
        effectEndTime: Long
    ): DrinkSession?

    // select current drink session for user
    @Query("""
        SELECT *
        FROM drink_session_table
        WHERE userId = :userId
        AND startTimestamp <= :currentTime
        AND endTimeStamp >= :currentTime
        ORDER BY startTimestamp DESC
        LIMIT 1
    """)
    fun getCurrentDrinkSessionFlow(userId: Int, currentTime: Long): Flow<DrinkSession?>
}
