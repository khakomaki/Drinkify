package com.example.drinkify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.drinkify.core.database.DrinkDatabase
import com.example.drinkify.drinks.DrinkViewModel
import com.example.drinkify.navi.AppNavigation
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            DrinkDatabase::class.java,
            name = "drinkify.db"
        )
        .fallbackToDestructiveMigration()
        .build()
    }

    private val viewModel: DrinkViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return DrinkViewModel(db.drinkDao, db.userDao) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // default user
        loadDefaultUser()

        setContent {
            AppNavigation(viewModel = viewModel)
        }
    }

    private fun loadDefaultUser() {
        lifecycleScope.launch {
            val existingUser = viewModel.getUser()
            if (existingUser == null) {
                viewModel.insertDefaultUser()
            }
        }
    }
}
