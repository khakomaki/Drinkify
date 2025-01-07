package com.example.drinkify.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import java.util.Locale

@Composable
fun BACMeter(bac: Double) {
    // use promille / per mille for certain countries
    val currentLocale = Locale.getDefault()
    val isPerMille = currentLocale.country in listOf("FI", "GE", "NL", "SE")
    val formattedBAC = if (isPerMille) {
        String.format(currentLocale,"%.2f", bac * 10.0) + "‰"
    } else {
        String.format(currentLocale,"%.3f", bac) + "%"
    }

    Text(
        text = "BAC: $formattedBAC",
        style = MaterialTheme.typography.headlineLarge,
        fontWeight = FontWeight.Bold
    )
}
