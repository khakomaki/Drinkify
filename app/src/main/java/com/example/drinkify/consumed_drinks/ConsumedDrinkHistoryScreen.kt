package com.example.drinkify.consumed_drinks

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
                Row(
                    modifier = Modifier.fillMaxWidth()
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
                        Text(
                            text = "Consumed: ${formatTimestamp(consumedDrink.timestamp)}",
                            fontSize = 12.sp
                        )
                    }

                    // deletion button
                    IconButton(onClick = {
                        onEvent(ConsumedDrinkEvent.ShowDeleteConfirmation(consumedDrink))
                    }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete drink record"
                        )
                    }
                }
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
