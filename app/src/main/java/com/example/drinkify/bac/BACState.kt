package com.example.drinkify.bac

data class BACState(
    val bac: Double = 0.0,
    val sessionLength: Long = 0,
    val drinkImages: List<Int> = emptyList()
)
