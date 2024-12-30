package com.example.drinkify.bac

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.drinkify.core.database.ConsumedDrinkDao
import com.example.drinkify.core.database.UserDao
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
            combine(
                userDao.getUserFlow(),
                consumedDrinkDao.getConsumedDrinksAdvanced(0)
            ) { user, drinks ->
                val bac = BACCalculator.calculateBAC(
                    consumedDrinks = drinks,
                    weightKg = user.weightKg,
                    isMale = true // TODO figure out gender policy
                )
                BACState(bac = bac)
            }.collect { updatedState ->
                _state.value = updatedState
            }
        }
    }
}
