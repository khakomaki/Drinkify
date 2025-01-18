package com.example.drinkify.stats

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.example.drinkify.ui.components.BasicTopBar
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun StatsScreen(
    state: StatsState,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            BasicTopBar(
                title = "Stats",
                onNavigateBack = onNavigateBack
            )
        },
        modifier = Modifier.padding(16.dp)
    ) { padding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .padding(padding)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            // total drink count
            StatItem(title = "Total Drink Count") {
                Text(
                    text = state.totalDrinkCount.toString(),
                    fontSize = 20.sp
                )
            }

            // total consumed alcohol
            StatItem(
                title = "Total Consumed Alcohol"
            ) {
                Text(
                    text = "${state.totalConsumedAlcohol}ml",
                    fontSize = 20.sp
                )
            }

            // most consumed drink
            StatItem(
                title = "Most Consumed Drink"
            ) {
                state.mostConsumedDrink?.let { drink ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically
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
                    }
                } ?: Text(
                    text = "No drinks have been consumed yet",
                    fontSize = 20.sp
                )
            }
        }
    }
}
