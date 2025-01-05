package com.example.drinkify.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.drinkify.core.database.UserDao
import com.example.drinkify.core.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProfileViewModel(private val userDao: UserDao): ViewModel() {
    private val _state = MutableStateFlow(ProfileState())
    val state = _state.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), ProfileState())

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            val user = userDao.getUser()
            if (user == null) {
                // handle default user scenario
                createDefaultUser()
            } else {
                // existing user
                _state.value = _state.value.copy(
                    name = user.name,
                    gender = user.gender,
                    weight = user.weightKg
                )
            }
        }
    }

    private suspend fun createDefaultUser() {
        val defaultUser = User(
            id = 0,
            name = "User",
            gender = Gender.MALE,
            weightKg = 70f
        )
        userDao.upsertUser(defaultUser)
        _state.value = _state.value.copy(
            name = defaultUser.name,
            gender = Gender.MALE,
            weight = defaultUser.weightKg
        )
    }

    fun onEvent(event: ProfileEvent) {
        when (event) {
            // name
            is ProfileEvent.UpdateName -> {
                _state.value = _state.value.copy(name = event.name)
            }

            // sex
            is ProfileEvent.UpdateGender -> {
                _state.value = _state.value.copy(gender = event.gender)
            }

            // weight
            is ProfileEvent.UpdateWeight -> {
                _state.value = _state.value.copy(weight = event.weight)
            }

            // save profile
            ProfileEvent.SaveProfile -> {
                val name = _state.value.name
                val gender = _state.value.gender
                val weight = _state.value.weight
                // validate profile
                if (!isProfileValid(name, weight)) return
                val user = User(
                    id = 0,
                    name = name,
                    gender = gender,
                    weightKg = weight
                )
                viewModelScope.launch { userDao.upsertUser(user) }
            }
        }
    }

    private fun isProfileValid(name: String, weight: Float): Boolean {
        // name
        if (name.isBlank()) {
            return false
        }

        // weight
        if (weight <= 0) {
            return false
        }

        return true
    }
}