package com.example.drinkify.drinks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
            Text(text = "Add drink")
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextField(
                    value = state.name,
                    onValueChange = {
                        onEvent(DrinkEvent.setName(it))
                    },
                    placeholder = {
                        Text(text = "Drink name")
                    }
                )
                TextField(
                    value = state.amountInMl.toString(),
                    onValueChange = {
                        onEvent(DrinkEvent.setAmountMl(it.toIntOrNull() ?: 0))
                    },
                    placeholder = {
                        Text(text = "Consumed amount in ml")
                    }
                )
                TextField(
                    value = state.alcoholPercentage.toString(),
                    onValueChange = {
                        onEvent(DrinkEvent.setAlcoholPercentage(it.toFloatOrNull() ?: 0f))
                    },
                    placeholder = {
                        Text(text = "Drink percentage")
                    }
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                onEvent(DrinkEvent.saveDrink)
            }) {
                Text(text = "Save")
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