package com.example.drinkify.core.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "drink_session_table")
data class DrinkSession(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int,
    val name: String = "Drink Session",
    val startTimestamp: Long,
    val endTimeStamp: Long
)
