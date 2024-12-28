package com.example.drinkify.navi

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.drinkify.drinks.DrinkScreen
import com.example.drinkify.drinks.DrinkViewModel
import com.example.drinkify.mainscreen.MainScreen

@Composable
fun AppNavigation(viewModel: DrinkViewModel) {
    val state by viewModel.state.collectAsState()
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            MainScreen(
                BAC = state.BAC,
                onNavigateToDrinks = { navController.navigate("drinks") }
            )
        }
        composable("drinks") {
            DrinkScreen(
                state = state,
                onEvent = viewModel::onEvent
            )
        }
    }
}