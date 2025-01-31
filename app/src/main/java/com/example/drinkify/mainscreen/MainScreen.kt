package com.example.drinkify.mainscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Liquor
import androidx.compose.material.icons.filled.StackedBarChart
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.drinkify.bac.BACState
import com.example.drinkify.ui.components.BACMeter
import com.example.drinkify.ui.components.MainTopBar
import com.example.drinkify.ui.components.NavigationButton

@Composable
fun MainScreen(
    bacState: BACState,
    onNavigateToDrinks: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToRecordDrink: () -> Unit,
    onNavigateToDrinkHistory: () -> Unit,
    onNavigateToStats: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        // main top bar
        topBar = {
            MainTopBar(
                onNavigateToProfile = onNavigateToProfile
            )
        },

        // BAC Meter
        bottomBar = {
            BottomAppBar(
                modifier = Modifier.padding(bottom = 32.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    BACMeter(
                        bac = bacState.bac,
                        sessionLength = bacState.sessionLength,
                        drinkImages = bacState.drinkImages
                    )
                }
            }
        },
        modifier = modifier
    ) { padding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(padding)
                .fillMaxWidth()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Navigation
            NavigationButton(
                text = "Drinks",
                icon = Icons.Default.Edit,
                onClick = onNavigateToDrinks
            )
            NavigationButton(
                text = "Record Drink",
                icon = Icons.Default.Liquor,
                onClick = onNavigateToRecordDrink
            )
            NavigationButton(
                text = "Drink History",
                icon = Icons.Filled.History,
                onClick = onNavigateToDrinkHistory
            )
            NavigationButton(
                text = "Stats",
                icon = Icons.Default.StackedBarChart,
                onClick = onNavigateToStats
            )
        }
    }
}
