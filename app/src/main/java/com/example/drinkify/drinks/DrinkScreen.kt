package com.example.drinkify.drinks

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.drinkify.ui.components.BasicTopBar

@Composable
fun DrinkScreen(
    state: DrinkState,
    onEvent: (DrinkEvent) -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold (
        topBar = {
            BasicTopBar(
                title = "Edit Drinks",
                onNavigateBack = onNavigateBack
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onEvent(DrinkEvent.ShowDialog)
            }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add drink"
                )
            }
        },
        modifier = Modifier.padding(16.dp)
    ) { padding ->
        // add drink dialog
        if(state.isAddingDrink) {
            AddDrinkDialog(state = state, onEvent = onEvent)
        }
        // delete drink dialog
        if(state.isDeletingDrink) {
            DeleteDrinkDialog(state = state, onEvent = onEvent)
        }

        // list of sort methods
        LazyColumn(
            contentPadding = padding,
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item{
                Row (
                    modifier = Modifier
                        .fillMaxSize()
                        .horizontalScroll(rememberScrollState()),
                    verticalAlignment = CenterVertically
                ) {
                    SortType.entries.forEach { sortType ->
                        Row (
                            modifier = Modifier
                                .clickable {
                                    onEvent(DrinkEvent.SortDrinks(sortType))
                                },
                                verticalAlignment = CenterVertically
                        ) {
                            RadioButton(
                                selected = state.sortType == sortType,
                                onClick = {
                                    onEvent(DrinkEvent.SortDrinks(sortType))
                                }
                            )
                            Text(text = sortType.displayName)
                        }
                    }
                }
            }

            // list of existing drinks
            items(state.drinks){ drink ->
                Row(
                    modifier = Modifier.fillMaxWidth()
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
                    // edit button
                    IconButton(onClick = {
                        onEvent(DrinkEvent.EditDrink(drink))
                    }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit drink"
                        )
                    }

                    // deletion button
                    IconButton(onClick = {
                        onEvent(DrinkEvent.ShowDeleteConfirmation(drink))
                    }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete drink"
                        )
                    }
                }

            }
        }
    }
}