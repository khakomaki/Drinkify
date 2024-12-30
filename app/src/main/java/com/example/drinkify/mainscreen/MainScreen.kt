package com.example.drinkify.mainscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MainScreen(
    onNavigateToDrinks: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToRecordDrink: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth().padding(16.dp)
    ) {
        // BAC meter
        Text(
            text = "BAC: ${"%.2f".format(15.5)}%",
            style = MaterialTheme.typography.headlineLarge
        )

        // Navigation
        Button(onClick = onNavigateToDrinks) {
            Text("Drinks")
        }
        Button(onClick = onNavigateToProfile) {
            Text("Profile")
        }
        Button(onClick = onNavigateToRecordDrink) {
            Text("Record Drinks")
        }
    }
}