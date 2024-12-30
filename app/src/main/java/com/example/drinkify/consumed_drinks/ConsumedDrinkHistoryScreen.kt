package com.example.drinkify.consumed_drinks

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import java.text.DateFormat

@Composable
fun ConsumedDrinkHistoryScreen(
    consumedDrinkState: ConsumedDrinkState,
    onEvent: (ConsumedDrinkEvent) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        items(consumedDrinkState.consumedDrinksAdvanced) { consumedDrink ->
            Row {
                Column {
                    Text(
                        text = consumedDrink.drinkName,
                        fontSize = 20.sp
                    )
                    Text(
                        text = "${consumedDrink.drinkAmountInMl}ml",
                        fontSize = 16.sp
                    )
                    Text(
                        text = "${consumedDrink.drinkAlcoholPercentage}%",
                        fontSize = 16.sp
                    )
                    Text(
                        text = "Consumed: ${formatTimestamp(consumedDrink.timestamp)}",
                        fontSize = 12.sp
                    )
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
