package com.example.drinkify.drinks

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DeleteDrinkDialog(
    state: DrinkState,
    onEvent: (DrinkEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    // return immediately if no drink is selected
    if(state.selectedDrink == null) {
        return
    }
    AlertDialog(
        onDismissRequest = { onEvent(DrinkEvent.hideDialog ) },
        title = { Text("Confirm deletion") },
        text = { Text("Do you want to delete \"${state.selectedDrink.name}\"?") },
        confirmButton = {
            TextButton(onClick = {
                onEvent(DrinkEvent.deleteDrink(state.selectedDrink))
                onEvent(DrinkEvent.hideDialog)
            }) {
                Text("Delete")
            }
        },
        dismissButton = {
            TextButton(onClick = { onEvent(DrinkEvent.hideDialog) }) {
                Text("Cancel")
            }
        }
    )
}