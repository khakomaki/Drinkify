package com.example.drinkify.bac

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.drinkify.core.database.ConsumedDrinkDao
import com.example.drinkify.core.database.UserDao
import com.example.drinkify.profile.Gender
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

    init {
        viewModelScope.launch {
            userDao.getUserFlow().combine(
                consumedDrinkDao.getConsumedDrinksAdvanced(0)
            ) { user, drinks ->
                // null check before calculating
                if (user != null) {
                    val bac = BACCalculator.calculateBAC(
                        consumedDrinks = drinks,
                        weightKg = user.weightKg,
                        isMale = user.gender == Gender.MALE
                    )
                    BACState(bac = bac)
                } else {
                    // default BAC if user is null
                    BACState(bac = 0.0)
                }
            }.collect { updatedState ->
                _state.value = updatedState
            }
        }
    }
}

