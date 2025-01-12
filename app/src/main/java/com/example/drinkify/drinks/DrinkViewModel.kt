package com.example.drinkify.drinks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.drinkify.core.models.Drink
import com.example.drinkify.core.database.DrinkDao
import com.example.drinkify.images.DrinkImages
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class DrinkViewModel(private val drinkDao: DrinkDao): ViewModel() {

    private val _sortType = MutableStateFlow(SortType.NAME)
    private val _drinks = _sortType
        .flatMapLatest { sortType ->
            when(sortType) {
                SortType.NAME -> drinkDao.getAllDrinksByName()
                SortType.AMOUNT_ML -> drinkDao.getAllDrinksByAmount()
                SortType.ALC_PERCENTAGE -> drinkDao.getAllDrinksByAlcoholPercentage()
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
            // editing drink
            is DrinkEvent.EditDrink -> {
                _state.update { it.copy(
                    isEditingDrink = true,
                    isAddingDrink = true,
                    selectedDrink = event.drink,
                    name = event.drink.name,
                    amountInMl = event.drink.amountInMl,
                    alcoholPercentage = event.drink.alcoholPercentage,
                    imageResId = event.drink.imageResId
                )}
            }

            // saving edited drink
            is DrinkEvent.SaveEditedDrink -> {
                // return immediately if no drink is selected
                val selectedDrink = _state.value.selectedDrink ?: return
                val editedDrink = selectedDrink.copy(
                    name = _state.value.name,
                    amountInMl = _state.value.amountInMl,
                    alcoholPercentage = _state.value.alcoholPercentage,
                    imageResId = _state.value.imageResId
                )
                // validate drink
                if(!isDrinkValid(
                        editedDrink.name,
                        editedDrink.amountInMl,
                        editedDrink.alcoholPercentage
                )) return
                // update edited drink
                viewModelScope.launch { drinkDao.upsertDrink(editedDrink) }
                // clear dialog
                clearDialogState()
            }

            // deleting drink
            is DrinkEvent.DeleteDrink -> {
                viewModelScope.launch {
                    drinkDao.deleteDrink(event.drink)
                }
            }

            // hiding dialog
            is DrinkEvent.HideDialog -> {
                clearDialogState()
            }

            // saving new drink
            is DrinkEvent.SaveDrink -> {
                val name = _state.value.name
                val amountInMl = _state.value.amountInMl
                val alcoholPercentage = _state.value.alcoholPercentage
                val imagePath = _state.value.imageResId
                // validate drink
                if(!isDrinkValid(name, amountInMl, alcoholPercentage)) return
                val drink = Drink(
                    name = name,
                    amountInMl = amountInMl,
                    alcoholPercentage = alcoholPercentage,
                    imageResId = imagePath
                )
                viewModelScope.launch { drinkDao.upsertDrink(drink) }
                // clear dialog
                clearDialogState()
            }

            is DrinkEvent.SetImage -> {
                _state.update { it.copy(
                    imageResId = event.imageResId
                ) }
            }

            // setting alcohol percentage
            is DrinkEvent.SetAlcoholPercentage -> {
                _state.update { it.copy(
                    alcoholPercentage = event.alcoholPercentage
                ) }
            }

            // setting amount
            is DrinkEvent.SetAmountMl -> {
                _state.update { it.copy(
                    amountInMl = event.amountMl
                ) }
            }

            // setting name
            is DrinkEvent.SetName -> {
                _state.update { it.copy(
                    name = event.name
                ) }
            }

            // showing drink dialog
            is DrinkEvent.ShowDialog -> {
                _state.update { it.copy(
                    isAddingDrink = true
                ) }
            }

            // sorting drinks
            is DrinkEvent.SortDrinks -> {
                _sortType.value = event.sortType
            }

            // confirming drink deletion
            is DrinkEvent.ShowDeleteConfirmation -> {
                _state.update { it.copy(
                    isDeletingDrink = true,
                    selectedDrink = event.drink
                )}
            }
        }
    }

    // validates if drink can be saved with the given values
    private fun isDrinkValid(name: String, amount: Int, percentage: Float): Boolean {
        // name
        if(name.isBlank()) {
            return false
        }
        // amount
        if(amount <= 0) {
            return false
        }
        // alcohol %
        if(percentage < 0 || 100 < percentage) {
            return false
        }
        // return true if drink is valid
        return true
    }

    // sets dialog state to default values
    private fun clearDialogState() {
        _state.update { it.copy(
            isEditingDrink = false,
            isAddingDrink = false,
            isDeletingDrink = false,
            name = "",
            amountInMl = 0,
            alcoholPercentage = 0f,
            selectedDrink = null,
            imageResId = DrinkImages.PLACEHOLDER.resId
        )}
    }
}
