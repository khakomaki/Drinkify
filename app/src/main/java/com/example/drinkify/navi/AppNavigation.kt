package com.example.drinkify.navi

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.drinkify.bac.BACViewModel
import com.example.drinkify.consumed_drinks.AvailableDrinkScreen
import com.example.drinkify.consumed_drinks.ConsumedDrinkHistoryScreen
import com.example.drinkify.consumed_drinks.ConsumedDrinkViewModel
import com.example.drinkify.drinks.DrinkScreen
import com.example.drinkify.drinks.DrinkViewModel
import com.example.drinkify.mainscreen.MainScreen
import com.example.drinkify.profile.ProfileScreen
import com.example.drinkify.profile.ProfileViewModel

@Composable
fun AppNavigation(
    drinkViewModel: DrinkViewModel,
    profileViewModel: ProfileViewModel,
    consumedDrinkViewModel: ConsumedDrinkViewModel,
    bacViewModel: BACViewModel
) {
    val bacState by bacViewModel.state.collectAsState()
    val drinkState by drinkViewModel.state.collectAsState()
    val profileState by profileViewModel.state.collectAsState()
    val consumedDrinkState by consumedDrinkViewModel.state.collectAsState()
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            MainScreen(
                bacState = bacState,
                onNavigateToDrinks = { navController.navigate("drinks") },
                onNavigateToProfile = { navController.navigate("profile") },
                onNavigateToRecordDrink = { navController.navigate("record_drinks") },
                onNavigateToDrinkHistory = { navController.navigate("drink_history")}
            )
        }
        composable("drinks") {
            DrinkScreen(
                state = drinkState,
                onEvent = drinkViewModel::onEvent,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable("profile") {
            ProfileScreen(
                state = profileState,
                onEvent = profileViewModel::onEvent,
                viewModel = profileViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable("record_drinks") {
            AvailableDrinkScreen(
                drinkState = drinkState,
                consumedDrinkState = consumedDrinkState,
                onEvent = consumedDrinkViewModel::onEvent,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable("drink_history") {
            ConsumedDrinkHistoryScreen(
                consumedDrinkState = consumedDrinkState,
                onEvent = consumedDrinkViewModel::onEvent,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}