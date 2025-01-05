package com.example.drinkify.profile

sealed interface ProfileEvent {
    data object SaveProfile: ProfileEvent

    data class UpdateName(val name: String): ProfileEvent
    data class UpdateGender(val gender: Gender): ProfileEvent
    data class UpdateWeight(val weight: Float): ProfileEvent
}
