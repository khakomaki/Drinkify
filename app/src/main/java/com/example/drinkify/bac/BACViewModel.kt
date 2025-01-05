package com.example.drinkify.bac

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.drinkify.core.database.ConsumedDrinkDao
import com.example.drinkify.core.database.UserDao
import com.example.drinkify.profile.Gender
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class BACViewModel(
    private val consumedDrinkDao: ConsumedDrinkDao,
    private val userDao: UserDao
): ViewModel() {
    private val _state = MutableStateFlow(BACState())
    val state: StateFlow<BACState> = _state.asStateFlow()

    private val updateIntervalMs = 60_000L // 1 minute
    private val timeFlow = MutableStateFlow(System.currentTimeMillis())

    init {
        startBACUpdates()
    }

    // periodically update BAC
    private fun startBACUpdates() {
        viewModelScope.launch {
            while (true) {
                timeFlow.value = System.currentTimeMillis() // Emit current time
                delay(updateIntervalMs) // Delay for 1 minute
            }
        }

        // combine time flow with user data and consumed drinks
        viewModelScope.launch {
            timeFlow
                .combine(userDao.getUserFlow()) { _, user -> user }
                .combine(consumedDrinkDao.getConsumedDrinksAdvanced(0)) { user, drinks ->
                    if (user == null) {
                        // use default if user is not available
                        BACState(bac = 0.0)
                    } else {
                        // recalculate BAC
                        val bac = BACCalculator.calculateBAC(
                            consumedDrinks = drinks,
                            weightKg = user.weightKg,
                            isMale = user.gender == Gender.MALE
                        )
                        BACState(bac = bac)
                    }
                }
                .collect { updatedState ->
                    _state.value = updatedState
                }
        }
    }
}

