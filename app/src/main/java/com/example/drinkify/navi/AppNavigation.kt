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
import com.example.drinkify.profile.ProfileViewModel

@Composable
fun AppNavigation(
    drinkViewModel: DrinkViewModel,
    profileViewModel: ProfileViewModel
) {
    val drinkState by drinkViewModel.state.collectAsState()
    val profileState by profileViewModel.state.collectAsState()
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            MainScreen(
                onNavigateToDrinks = { navController.navigate("drinks") },
                onNavigateToProfile = { navController.navigate("profile") }
            )
        }
        composable("drinks") {
            DrinkScreen(
                state = drinkState,
                onEvent = drinkViewModel::onEvent
            )
        }

        composable("profile") {
            ProfileScreen(
                state = profileState,
                onEvent = profileViewModel::onEvent
            )
        }
    }
}