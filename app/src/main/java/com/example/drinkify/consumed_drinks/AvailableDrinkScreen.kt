package com.example.drinkify.consumed_drinks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.drinkify.drinks.DrinkItem
import com.example.drinkify.drinks.DrinkState
import com.example.drinkify.ui.components.BasicTopBar
import com.example.drinkify.ui.components.DrinkTimePickerDialog

@Composable
fun AvailableDrinkScreen(
    drinkState: DrinkState,
    consumedDrinkState: ConsumedDrinkState,
    onEvent: (ConsumedDrinkEvent) -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            BasicTopBar(
                title = "Record Drinks",
                onNavigateBack = onNavigateBack
            )
        },
        modifier = Modifier.padding(16.dp)
    ) { padding ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = padding
        ) {
            items(drinkState.drinks) { drink ->
                DrinkItem(
                    drink = drink,
                    onRecordNow = {
                        val time = System.currentTimeMillis()
                        onEvent(ConsumedDrinkEvent.SelectDrink(drink))
                        onEvent(ConsumedDrinkEvent.SetConsumptionTime(time))
                        onEvent(ConsumedDrinkEvent.SaveConsumedDrink)
                        onEvent(ConsumedDrinkEvent.HideTimePicker)
                    },
                    onSelectTime = {
                        onEvent(ConsumedDrinkEvent.SelectDrink(drink))
                        onEvent(ConsumedDrinkEvent.ShowTimePicker)
                    }
                )
            }
        }

        // drink picking dialog
        if (consumedDrinkState.isPickingTime) {
            // ensure drink is selected
            if (consumedDrinkState.selectedDrink == null) return@Scaffold
            DrinkTimePickerDialog(
                initialTime = System.currentTimeMillis(),
                onConfirm = { timestamp ->
                    onEvent(ConsumedDrinkEvent.SetConsumptionTime(timestamp))
                    onEvent(ConsumedDrinkEvent.SaveConsumedDrink)
                    onEvent(ConsumedDrinkEvent.HideTimePicker)
                            },
                onDismiss = { onEvent(ConsumedDrinkEvent.HideTimePicker) },
                drinkName = consumedDrinkState.selectedDrink.name
            )
        }
    }
}
