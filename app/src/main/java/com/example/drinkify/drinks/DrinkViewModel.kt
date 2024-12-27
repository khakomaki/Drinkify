package com.example.drinkify.drinks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.drinkify.core.models.Drink
import com.example.drinkify.core.database.DrinkDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class DrinkViewModel(private val dao: DrinkDao): ViewModel() {

    private val _sortType = MutableStateFlow(SortType.NAME)
    private val _drinks = _sortType
        .flatMapLatest { sortType ->
            when(sortType) {
                SortType.NAME -> dao.getAllDrinksByName()
                SortType.AMOUNT_ML -> dao.getAllDrinksByAmount()
                SortType.ALC_PERCENTAGE -> dao.getAllDrinksByAlcoholPercentage()
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _state = MutableStateFlow(DrinkState())
    val state = combine(_state, _sortType, _drinks) { state, sortType, drinks ->
        state.copy(
            drinks = drinks,
            sortType = sortType
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000), DrinkState())

    fun onEvent(event: DrinkEvent) {
        when(event) {
            is DrinkEvent.deleteDrink -> {
                viewModelScope.launch {
                    dao.deleteDrink(event.drink)
                }
            }
            DrinkEvent.hideDialog -> {
                _state.update { it.copy(
                    isAddingDrink = false
                ) }
            }
            DrinkEvent.saveDrink -> {
                val name = _state.value.name
                val amountInMl = _state.value.amountInMl
                val alcoholPercentage = _state.value.alcoholPercentage

                if(name.isBlank() || amountInMl <= 0 || alcoholPercentage < 0 || 100 < alcoholPercentage) {
                    return
                }

                val drink = Drink(
                    name = name,
                    amountInMl = amountInMl,
                    alcoholPercentage = alcoholPercentage
                )
                viewModelScope.launch {
                    dao.upsertDrink(drink)
                }
                _state.update { it.copy(
                    isAddingDrink = false,
                    name = "",
                    amountInMl = 0,
                    alcoholPercentage = 0f
                )}
            }
            is DrinkEvent.setAlcoholPercentage -> {
                _state.update { it.copy(
                    alcoholPercentage = event.alcoholPercentage
                ) }
            }
            is DrinkEvent.setAmountMl -> {
                _state.update { it.copy(
                    amountInMl = event.amountMl
                ) }
            }
            is DrinkEvent.setName -> {
                _state.update { it.copy(
                    name = event.name
                ) }
            }
            DrinkEvent.showDialog -> {
                _state.update { it.copy(
                    isAddingDrink = true
                ) }
            }
            is DrinkEvent.sortDrinks -> {
                _sortType.value = event.sortType
            }
        }
    }
}