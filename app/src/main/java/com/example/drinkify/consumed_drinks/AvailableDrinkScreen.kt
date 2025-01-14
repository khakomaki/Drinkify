package com.example.drinkify.consumed_drinks

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = MaterialTheme.shapes.medium
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    // drink image
                    Column(
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Image(
                            painter = painterResource(id = drink.imageResId),
                            contentDescription = "Drink image",
                            modifier = Modifier.size(50.dp)
                        )
                    }

                    // drink information
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = drink.name,
                            fontSize = 20.sp
                        )
                        Text(
                            text = "${drink.amountInMl}ml",
                            fontSize = 14.sp
                        )
                        Text(
                            text = "${drink.alcoholPercentage}%",
                            fontSize = 14.sp
                        )
                    }

                    // record buttons
                    Column(
                        modifier = Modifier.padding(8.dp)
                    ) {
                        // record now button
                        Button(onClick = {
                            val time = System.currentTimeMillis()
                            // apply current time and selection
                            onEvent(ConsumedDrinkEvent.SetConsumptionTime(time))
                            onEvent(ConsumedDrinkEvent.SelectDrink(drink))
                            // save
                            onEvent(ConsumedDrinkEvent.SaveConsumedDrink)
                        }) {
                            Text("Drink now")
                        }

                        // record any time button
                        Button(onClick = {
                            onEvent(ConsumedDrinkEvent.SelectDrink(drink))
                            onEvent(ConsumedDrinkEvent.ShowTimePicker)
                        }) {
                            Text("Select time")
                        }
                    }
                }
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
