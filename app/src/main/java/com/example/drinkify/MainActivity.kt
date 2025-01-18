package com.example.drinkify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.drinkify.bac.BACViewModel
import com.example.drinkify.consumed_drinks.ConsumedDrinkViewModel
import com.example.drinkify.core.database.DrinkifyDatabase
import com.example.drinkify.drinks.DrinkViewModel
import com.example.drinkify.navi.AppNavigation
import com.example.drinkify.profile.ProfileViewModel
import com.example.drinkify.stats.StatsViewModel

@Suppress("UNCHECKED_CAST")
class MainActivity : ComponentActivity() {
    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            DrinkifyDatabase::class.java,
            name = "drinkify.db"
        )
        .fallbackToDestructiveMigration()
        .build()
    }

    private val consumedDrinkViewModel: ConsumedDrinkViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ConsumedDrinkViewModel(
                    db.consumedDrinkDao,
                    db.drinkSessionDao
                ) as T
            }
        }
    }

    private val drinkViewModel: DrinkViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return DrinkViewModel(db.drinkDao) as T
            }
        }
    }

    private val profileViewModel: ProfileViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ProfileViewModel(db.userDao) as T
            }
        }
    }

    private val bacViewModel: BACViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return BACViewModel(
                    consumedDrinkDao = db.consumedDrinkDao,
                    userDao = db.userDao
                ) as T
            }
        }
    }

    private val statsViewModel: StatsViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return StatsViewModel(db.consumedDrinkDao) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNavigation(
                drinkViewModel = drinkViewModel,
                profileViewModel = profileViewModel,
                consumedDrinkViewModel = consumedDrinkViewModel,
                bacViewModel = bacViewModel,
                statsViewModel = statsViewModel
            )
        }
    }
}
