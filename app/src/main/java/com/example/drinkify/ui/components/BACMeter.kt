package com.example.drinkify.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun BACMeter(bac: Double) {
    Text(
        text = "BAC: ${"%.2f".format(bac)}",
        style = MaterialTheme.typography.headlineLarge
    )
}
