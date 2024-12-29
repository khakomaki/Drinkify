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
            _state.value = _state.value.copy(
                name = user?.name ?: "",
                sex = user?.sex ?: "",
                weight = user?.weightKg ?: 0f
            )
        }
    }

    fun onEvent(event: ProfileEvent) {
        when (event) {
            // name
            is ProfileEvent.UpdateName -> {
                _state.value = _state.value.copy(name = event.name)
            }

            // sex
            is ProfileEvent.UpdateSex -> {
                _state.value = _state.value.copy(sex = event.sex)
            }

            // weight
            is ProfileEvent.UpdateWeight -> {
                _state.value = _state.value.copy(weight = event.weight)
            }

            // save profile
            ProfileEvent.SaveProfile -> {
                val name = _state.value.name
                val sex = _state.value.sex
                val weight = _state.value.weight
                // validate profile
                if (!isProfileValid(name, sex, weight)) return
                val user = User(
                    id = 0,
                    name = name,
                    sex = sex,
                    weightKg = weight
                )
                viewModelScope.launch { userDao.upsertUser(user) }
            }
        }
    }

    private fun isProfileValid(name: String, sex: String, weight: Float): Boolean {
        // TODO implement validation logic
        return true
    }
}