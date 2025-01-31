package com.example.drinkify.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.util.Locale

@Composable
fun BACMeter(bac: Double, sessionLength: Long, drinkImages: List<Int>) {

    // use promille / per mille for certain countries
    val currentLocale = Locale.getDefault()
    val isPerMille = currentLocale.country in listOf("FI", "GE", "NL", "SE")
    val formattedBAC = if (isPerMille) {
        String.format(currentLocale,"%.2f", bac * 10.0) + "‰"
    } else {
        String.format(currentLocale,"%.3f", bac) + "%"
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // BAC value
        Text(
            text = "BAC: $formattedBAC",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )

        // session length
        Text(
            text = String.format(
                currentLocale,
                "Session length: %d h %d min",
                sessionLength / 3600000, (sessionLength % 3600000) / 60000
            ),
            style = MaterialTheme.typography.bodyMedium
        )

        // icons
        LazyRow(
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(drinkImages) { imageResId ->
                Image(
                    painter = painterResource(id = imageResId),
                    contentDescription = null,
                    modifier = Modifier
                        .size(10.dp)
                        .background(MaterialTheme.colorScheme.surface)
                )
            }
        }
    }
}
