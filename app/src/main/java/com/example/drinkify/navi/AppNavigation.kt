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
import com.example.drinkify.profile.ProfileScreen

@Composable
fun AppNavigation(viewModel: DrinkViewModel) {
    val state by viewModel.state.collectAsState()
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            MainScreen(
                BAC = state.BAC,
                onNavigateToDrinks = { navController.navigate("drinks") },
                onNavigateToProfile = { navController.navigate("profile") }
            )
        }
        composable("drinks") {
            DrinkScreen(
                state = state,
                onEvent = viewModel::onEvent
            )
        }

        composable("profile") {
            ProfileScreen(
                name = state.user?.name ?: "",
                sex = state.user?.sex ?: "",
                weight = state.user?.weightKg ?: 0f,
                onSave = { name, sex, weight ->
                    viewModel.updateUser(name, sex, weight)
                    navController.popBackStack()
                }
            )
        }
    }
}