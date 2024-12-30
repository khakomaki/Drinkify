package com.example.drinkify.core.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "consumed_drink_table",
    foreignKeys = [
        ForeignKey(
            entity = Drink::class,
            parentColumns = ["id"],
            childColumns = ["drinkId"],
            // ensure deletion if base drink gets deleted
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            // ensure deletion if user gets deleted
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ConsumedDrink(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int,
    val drinkId: Int,
    val timestamp: Long
)
