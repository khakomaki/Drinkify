package com.example.drinkify.consumed_drinks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.drinkify.drinks.DrinkState
import com.example.drinkify.ui.components.BasicTopBar

@Composable
fun AvailableDrinkScreen(
    drinkState: DrinkState,
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
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // drink information
                    Column(
                        modifier = Modifier.weight(2f)
                    ) {
                        Text(
                            text = drink.name,
                            fontSize = 20.sp
                        )
                        Text(
                            text = "${drink.amountInMl}ml",
                            fontSize = 12.sp
                        )
                        Text(
                            text = "${drink.alcoholPercentage}%",
                            fontSize = 12.sp
                        )
                    }

                    // record button
                    Button(onClick = {
                        val time = System.currentTimeMillis()
                        // apply current time and selection
                        onEvent(ConsumedDrinkEvent.SetConsumptionTime(time))
                        onEvent(ConsumedDrinkEvent.SelectDrink(drink))
                        // save
                        onEvent(ConsumedDrinkEvent.SaveConsumedDrink)
                    }) {
                        Text("Drink")
                    }
                }
            }
        }
    }
}