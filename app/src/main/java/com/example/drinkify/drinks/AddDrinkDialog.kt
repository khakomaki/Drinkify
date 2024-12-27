package com.example.drinkify.drinks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AddDrinkDialog(
    state: DrinkState,
    onEvent: (DrinkEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = {
            onEvent(DrinkEvent.hideDialog)
        },
        title = {
            Text( if (state.isEditingDrink) "Edit drink" else "Add drink" )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // drink name
                TextField(
                    value = state.name,
                    onValueChange = {
                        onEvent(DrinkEvent.setName(it))
                    },
                    placeholder = { Text(text = "Drink name") }
                )

                // amount
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(0.5f)
                ) {
                    TextField(
                        value = state.amountInMl.toString(),
                        onValueChange = {
                            onEvent(DrinkEvent.setAmountMl(it.toIntOrNull() ?: 0))
                        },
                        placeholder = { Text(text = "Consumed amount in ml") },
                        modifier = Modifier.weight(1f)
                    )
                    Text(text = "ml")
                }
                // live validation for amount
                if(state.amountInMl <= 0) {
                    Text(text = "Enter a number greater than 0", color = Color.Red)
                }

                // alcohol percentage
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(0.5f)
                ) {
                    TextField(
                        value = state.alcoholPercentage.toString(),
                        onValueChange = {
                            onEvent(DrinkEvent.setAlcoholPercentage(it.toFloatOrNull() ?: 0f))
                        },
                        placeholder = { Text(text = "Drink percentage") },
                        modifier = Modifier.weight(1f)
                    )
                    Text(text = "%")
                }
                // live validation for percentage
                if(state.alcoholPercentage < 0 || 100 < state.alcoholPercentage) {
                    Text(text = "Enter a valid percentage", color = Color.Red)
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                onEvent(
                    if(state.isEditingDrink) DrinkEvent.saveEditedDrink else DrinkEvent.saveDrink
                )
            }) {
                Text( if(state.isEditingDrink) "Save changes" else "Save" )
            }
        },
        dismissButton = {
            Button(onClick = {
                onEvent(DrinkEvent.hideDialog)
            }) {
                Text(text = "Cancel")
            }
        }
    )
}