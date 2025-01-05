package com.example.drinkify.profile

enum class Gender {
    MALE,
    FEMALE,
    NOT_SPECIFIED
}

fun Gender.toUIVersion(): String {
    return when (this) {
        Gender.MALE -> "Male"
        Gender.FEMALE -> "Female"
        Gender.NOT_SPECIFIED -> "Not Specified"
    }
}