package com.example.drinkify.bac

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.drinkify.core.database.ConsumedDrinkDao
import com.example.drinkify.core.database.DrinkSessionDao
import com.example.drinkify.core.database.UserDao
import com.example.drinkify.profile.Gender
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class BACViewModel(
    private val consumedDrinkDao: ConsumedDrinkDao,
    private val drinkSessionDao: DrinkSessionDao,
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
                timeFlow.value = System.currentTimeMillis()
                delay(updateIntervalMs)
            }
        }

        // combine time flow with user data and consumed drinks
        viewModelScope.launch {
            timeFlow
                .combine(userDao.getUserFlow()) { _, user -> user }
                .combine(timeFlow) { user, _ ->
                    val currentTime = System.currentTimeMillis()
                    val currentSession = user?.let {
                        drinkSessionDao.getCurrentDrinkSessionFlow(
                            user.id,
                            currentTime
                        ).first()
                    }
                    Pair(user, currentSession)
                }
                .collectLatest { (user, currentSession) ->
                    // if no user or current session, use default
                    if (user == null || currentSession == null) {
                        _state.value = BACState()
                        return@collectLatest
                    }

                    val sessionDrinks = consumedDrinkDao.getConsumedDrinksInSession(
                        user.id,
                        currentSession.id
                    )
                    val bac = BACCalculator.calculateBAC(
                        consumedDrinks = sessionDrinks,
                        weightKg = user.weightKg,
                        isMale = user.gender == Gender.MALE
                    )
                    _state.value = BACState(
                        bac = bac,
                        sessionLength = System.currentTimeMillis() - currentSession.startTimestamp,
                        drinkImages = sessionDrinks.map { it.drink.imageResId }
                    )
                }
        }
    }

    suspend fun recalculateBAC() {
        val user = userDao.getUserFlow().first() ?: return
        val currentTime = System.currentTimeMillis()
        val currentSession = drinkSessionDao.getCurrentDrinkSessionFlow(
            user.id,
            currentTime
        ).first() ?: return

        val sessionDrinks = consumedDrinkDao.getConsumedDrinksInSession(
            user.id,
            currentSession.id
        )

        val bac = BACCalculator.calculateBAC(
            consumedDrinks = sessionDrinks,
            weightKg = user.weightKg,
            isMale = user.gender == Gender.MALE
        )
        _state.value = BACState(
            bac = bac,
            sessionLength = System.currentTimeMillis() - currentSession.startTimestamp,
            drinkImages = sessionDrinks.map { it.drink.imageResId }
        )
    }
}
