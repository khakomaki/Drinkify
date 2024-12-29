package com.example.drinkify.drinks

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun DeleteDrinkDialog(
    state: DrinkState,
    onEvent: (DrinkEvent) -> Unit
) {
    // return immediately if no drink is selected
    if(state.selectedDrink == null) {
        return
    }
    AlertDialog(
        onDismissRequest = { onEvent(DrinkEvent.HideDialog ) },
        title = { Text("Confirm deletion") },
        text = { Text("Do you want to delete \"${state.selectedDrink.name}\"?") },
        confirmButton = {
            TextButton(onClick = {
                onEvent(DrinkEvent.DeleteDrink(state.selectedDrink))
                onEvent(DrinkEvent.HideDialog)
            }) {
                Text("Delete")
            }
        },
        dismissButton = {
            TextButton(onClick = { onEvent(DrinkEvent.HideDialog) }) {
                Text("Cancel")
            }
        }
    )
}