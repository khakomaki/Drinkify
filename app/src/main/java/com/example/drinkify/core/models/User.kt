package com.example.drinkify.core.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.drinkify.profile.Gender

@Entity(tableName = "user_table")
data class User (
    @PrimaryKey
    val id: Int = 0,
    val name: String,
    val gender: Gender,
    val weightKg: Float,
    val imageURI: String? = null
)
