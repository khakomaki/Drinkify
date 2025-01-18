package com.example.drinkify.consumed_drinks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.drinkify.drinks.DrinkItem
import com.example.drinkify.ui.components.BasicTopBar
import java.text.DateFormat

@Composable
fun ConsumedDrinkHistoryScreen(
    consumedDrinkState: ConsumedDrinkState,
    onEvent: (ConsumedDrinkEvent) -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            BasicTopBar(
                title = "Drink History",
                onNavigateBack = onNavigateBack
            )
        },
        modifier = Modifier.padding(16.dp)
    ) { padding ->
        // delete drink dialog
        if(consumedDrinkState.isDeletingConsumedDrink) {
            DeleteConsumedDrinkDialog(state = consumedDrinkState, onEvent = onEvent)
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = padding
        ) {
            items(consumedDrinkState.consumedDrinksAdvanced) { consumedDrinkAdvanced ->
                val drink = consumedDrinkAdvanced.drink
                val consumedDrink = consumedDrinkAdvanced.consumedDrink
                DrinkItem(
                    drink = drink,
                    additionalInformation = {
                        Text(
                            text = "Time: ${formatTimestamp(consumedDrink.timestamp)}",
                            fontSize = 12.sp
                        )
                    },
                    onDelete = {
                        onEvent(ConsumedDrinkEvent.ShowDeleteConfirmation(consumedDrink))
                    }
                )
            }
        }
    }
}

fun formatTimestamp(timestamp: Long): String {
    val formatter = DateFormat.getDateTimeInstance(
        DateFormat.MEDIUM,
        DateFormat.SHORT
    )
    return formatter.format(timestamp)
}
