package com.example.drinkify.profile

sealed interface ProfileEvent {
    object saveProfile: ProfileEvent
    data class updateName(val name: String): ProfileEvent
    data class updateSex(val sex: String): ProfileEvent
    data class updateWeight(val weight: Float): ProfileEvent
}
