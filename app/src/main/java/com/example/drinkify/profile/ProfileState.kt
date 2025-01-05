package com.example.drinkify.profile

data class ProfileState(
    val name: String = "",
    val gender: Gender = Gender.MALE,
    val weight: Float = 0f
)
