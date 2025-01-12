package com.example.drinkify.core.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.drinkify.images.DrinkImages

@Entity(tableName = "drink_table")
data class Drink(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val amountInMl: Int,
    val alcoholPercentage: Float,
    val imageResId: Int = DrinkImages.PLACEHOLDER.resId
)
