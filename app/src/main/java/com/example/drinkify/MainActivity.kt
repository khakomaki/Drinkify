package com.example.drinkify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.example.drinkify.model.Drink
import com.example.drinkify.model.DrinkDatabase
import com.example.drinkify.ui.theme.DrinkifyTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var database: DrinkDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        // Initialize database
        database = DrinkDatabase.getDatabase(this)

        setContent {
            DrinkifyTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(
                        modifier = Modifier.padding(innerPadding),
                        database = database
                    )
                }
            }
        }
    }
}

@Composable
fun MainScreen(modifier: Modifier = Modifier, database: DrinkDatabase) {
    var drinks by remember { mutableStateOf(listOf<Drink>()) }

    // Load drinks when the screen loads
    LaunchedEffect(Unit) {
        drinks = database.drinkDAO().getAllDrinks()
    }

    Column(modifier = modifier) {
        Button(onClick = {
            // Add a sample drink to the database
            val drink = Drink(name = "Beer", sizeInMl = 330, alcoholPercentage = 5.0f)
            LaunchedEffect(Unit) {
                database.drinkDAO().insertDrink(drink)
                drinks = database.drinkDAO().getAllDrinks()
            }
        }) {
            Text("Add Drink")
        }

        Text("Drinks in the database:")
        drinks.forEach { drink ->
            Text(text = "Name: ${drink.name}, Size: ${drink.sizeInMl}ml, Alcohol: ${drink.alcoholPercentage}%")
        }
    }
}
