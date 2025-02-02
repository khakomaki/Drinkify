package com.example.drinkify.drinks

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.drinkify.images.DrinkImages

@Composable
fun AddDrinkDialog(
    state: DrinkState,
    onEvent: (DrinkEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = {
            onEvent(DrinkEvent.HideDialog)
        },
        title = {
            Text( if (state.isEditingDrink) "Edit drink" else "Add drink" )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // drink name
                TextField(
                    value = state.name,
                    onValueChange = {
                        onEvent(DrinkEvent.SetName(it))
                    },
                    placeholder = { Text(text = "Drink name") }
                )
                // live validation for name
                if(state.name.isBlank()) {
                    Text(text = "Type name for drink", color = Color.Red)
                } else {
                    Text(text = "", color = Color.Transparent)
                }

                // amount, alcohol percentage and image
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // amount and alcohol percentage
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        // amount
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextField(
                                value = state.amountInMl.toString(),
                                onValueChange = {
                                    onEvent(DrinkEvent.SetAmountMl(it.toIntOrNull() ?: 0))
                                },
                                placeholder = { Text(text = "Consumed amount in ml") },
                                modifier = Modifier.weight(1f),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                            )
                            Text(text = "ml")
                        }
                        // live validation for amount
                        if(state.amountInMl <= 0) {
                            Text(text = "Enter a number greater than 0", color = Color.Red)
                        } else {
                            Text(text = "", color = Color.Transparent)
                        }

                        // alcohol percentage
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextField(
                                value = state.alcoholPercentage.toString(),
                                onValueChange = {
                                    onEvent(DrinkEvent.SetAlcoholPercentage(it.toFloatOrNull() ?: 0f))
                                },
                                placeholder = { Text(text = "Drink percentage") },
                                modifier = Modifier.weight(1f),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                            )
                            Text(text = "%")
                        }
                        // live validation for percentage
                        if(state.alcoholPercentage < 0 || 100 < state.alcoholPercentage) {
                            Text(text = "Enter a valid percentage", color = Color.Red)
                        } else {
                            Text(text = "", color = Color.Transparent)
                        }
                    }

                    // current drink image
                    Image(
                        painter = painterResource(id = state.imageResId),
                        contentDescription = "Drink image",
                        modifier = Modifier.size(100.dp)
                    )
                }

                // image selection
                Text(text = "Image")
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(DrinkImages.entries) { image ->
                        Image(
                            painter = painterResource(id = image.resId),
                            contentDescription = image.name,
                            modifier = Modifier
                                .size(100.dp)
                                .clickable {
                                    onEvent(DrinkEvent.SetImage(image.resId))
                                }
                                .border(
                                    BorderStroke(
                                        2.dp,
                                        // visual indication for selected image
                                        if (state.imageResId == image.resId) {
                                            Color.Blue
                                        } else {
                                            Color.Transparent
                                        }
                                    ),
                                )
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                onEvent(
                    if(state.isEditingDrink) DrinkEvent.SaveEditedDrink else DrinkEvent.SaveDrink
                )
            }) {
                Text( if(state.isEditingDrink) "Save changes" else "Save" )
            }
        },
        dismissButton = {
            Button(onClick = {
                onEvent(DrinkEvent.HideDialog)
            }) {
                Text(text = "Cancel")
            }
        }
    )
}
