package com.example.drinkify.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.drinkify.core.database.ConsumedDrinkDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StatsViewModel(private val consumedDrinkDao: ConsumedDrinkDao): ViewModel() {
    private val _state = MutableStateFlow(StatsState())
    val state = _state.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        StatsState()
    )

    // initialize stats
    init {
        // get total drink count
        viewModelScope.launch {
            consumedDrinkDao.getConsumedDrinkCount(userId = 0).collect { count ->
                _state.update { it.copy(totalDrinkCount = count) }
            }
        }

        // get total consumed alcohol
        viewModelScope.launch {
            consumedDrinkDao.getTotalConsumedAlcohol(userId = 0).collect { alcohol ->
                _state.update { it.copy(totalConsumedAlcohol = alcohol) }
            }
        }

        // get most consumed drink
        viewModelScope.launch {
            consumedDrinkDao.getMostConsumedDrink(userId = 0).collect { drink ->
                _state.update { it.copy(mostConsumedDrink = drink) }
            }
        }
    }
}
