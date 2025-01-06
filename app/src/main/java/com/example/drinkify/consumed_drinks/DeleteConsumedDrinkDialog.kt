package com.example.drinkify.consumed_drinks

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun DeleteConsumedDrinkDialog(
    state: ConsumedDrinkState,
    onEvent: (ConsumedDrinkEvent) -> Unit
) {
    // return immediately if no consumed drink is selected
    val consumedDrink = state.selectedConsumedDrink ?: return

    AlertDialog(
        onDismissRequest = { onEvent(ConsumedDrinkEvent.HideDialog ) },
        title = { Text("Confirm deletion") },
        text = { Text("Do you want to delete record of drinking\"${consumedDrink.drinkId}\"?") },
        confirmButton = {
            TextButton(onClick = {
                onEvent(ConsumedDrinkEvent.DeleteConsumedDrink(consumedDrink))
                onEvent(ConsumedDrinkEvent.HideDialog)
            }) {
                Text("Delete")
            }
        },
        dismissButton = {
            TextButton(onClick = { onEvent(ConsumedDrinkEvent.HideDialog) }) {
                Text("Cancel")
            }
        }
    )
}