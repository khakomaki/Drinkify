package com.example.drinkify.drinks

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.drinkify.core.models.Drink

@Composable
fun DrinkItem(
    drink: Drink,
    modifier: Modifier = Modifier,
    showImage: Boolean = true,
    showDetails: Boolean = true,
    onEdit: (() -> Unit)? = null,
    onDelete: (() -> Unit)? = null,
    onRecordNow: (() -> Unit)? = null,
    onSelectTime: (() -> Unit)? = null,
    additionalInformation: (@Composable () -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = MaterialTheme.shapes.medium
            )
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // image
        if (showImage) {
            Image(
                painter = painterResource(id = drink.imageResId),
                contentDescription = "Drink image",
                modifier = Modifier.size(50.dp)
            )
        }

        // information
        Column(
            modifier = Modifier.weight(1f)
        ) {
            // basic information
            if (showDetails) {
                Text(
                    text = drink.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${drink.amountInMl}ml",
                    fontSize = 14.sp
                )
                Text(
                    text = "${drink.alcoholPercentage}%",
                    fontSize = 14.sp
                )

                // additional information
                additionalInformation?.invoke()
            }
        }

        // action buttons
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            // edit
            if (onEdit != null) {
                IconButton(onClick = onEdit) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit drink"
                    )
                }
            }

            // delete
            if (onDelete != null) {
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete drink"
                    )
                }
            }

            // record buttons
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                // record now
                if (onRecordNow != null) {
                    Button(onClick = onRecordNow) {
                        Text("Drink now")
                    }
                }

                // select time
                if (onSelectTime != null) {
                    Button(onClick = onSelectTime) {
                        Text("Select time")
                    }
                }
            }
        }
    }
}
