package com.example.drinkify.core.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User (
    @PrimaryKey
    val id: Int = 0,
    val name: String,
    val sex: String,
    val weightKg: Float
)