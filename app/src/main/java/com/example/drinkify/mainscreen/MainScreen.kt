package com.example.drinkify.mainscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.drinkify.bac.BACState
import com.example.drinkify.ui.components.BACMeter

@Composable
fun MainScreen(
    bacState: BACState,
    onNavigateToDrinks: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToRecordDrink: () -> Unit,
    onNavigateToDrinkHistory: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth().padding(16.dp)
    ) {
        // BAC meter
        BACMeter(bac = bacState.bac)

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
        Button(onClick = onNavigateToDrinkHistory) {
            Text("Drink History")
        }
    }
}