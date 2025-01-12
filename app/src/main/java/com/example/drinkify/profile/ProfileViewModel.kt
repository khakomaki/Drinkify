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

    // state for managing changes
    private var initialState: ProfileState? = null

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            val user = userDao.getUser()
            // create default or use existing
            if (user == null) {
                createDefaultUser()
            } else {
                // update state
                _state.value = _state.value.copy(
                    name = user.name,
                    gender = user.gender,
                    weight = user.weightKg
                )
                // update initial state
                initialState = _state.value
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
        // insert default user
        userDao.upsertUser(defaultUser)
        _state.value = _state.value.copy(
            name = defaultUser.name,
            gender = Gender.MALE,
            weight = defaultUser.weightKg
        )
        // update initial state
        initialState = _state.value
    }

    private fun isModified(): Boolean {
        return _state.value != initialState
    }

    fun onEvent(event: ProfileEvent) {
        when (event) {
            // name
            is ProfileEvent.UpdateName -> {
                _state.value = _state.value.copy(
                    name = event.name
                )
            }

            // sex
            is ProfileEvent.UpdateGender -> {
                _state.value = _state.value.copy(
                    gender = event.gender
                )
            }

            // weight
            is ProfileEvent.UpdateWeight -> {
                _state.value = _state.value.copy(
                    weight = event.weight
                )
            }

            // save profile
            ProfileEvent.SaveProfile -> {
                if (!isProfileSaveValid()) return
                val name = _state.value.name
                val gender = _state.value.gender
                val weight = _state.value.weight
                val user = User(
                    id = 0,
                    name = name,
                    gender = gender,
                    weightKg = weight
                )
                viewModelScope.launch {
                    userDao.upsertUser(user)
                    // update initial state
                    initialState = _state.value
                }
            }
        }
    }

    fun isProfileSaveValid(): Boolean {
        val currentState = _state.value
        return isModified() && isProfileValid(currentState.name, currentState.weight)
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
