package com.example.sincerity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.sincerity.room.database.SincerityDatabase
import com.example.sincerity.ui.theme.SincerityTheme

class MainActivity : ComponentActivity() {
    private val database by lazy {
        Room.databaseBuilder(
            applicationContext,
            SincerityDatabase::class.java,
            "sincerity.db"
        ).build()
    }

    private val viewModel by viewModels<SincerityViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return SincerityViewModel(
                        database.userDao,
                        database.cardDao,
                        database.questionDao
                    ) as T
                }
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SincerityTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val state by viewModel.state.collectAsState()
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = "UsersScreen") {
                        composable("UsersScreen") {
                            UsersScreen(
                                state = state,
                                navController = navController,
                                onEvent = viewModel::onUserEvent
                            )
                        }
                        composable("AddUserScreen") {
                            AddUserScreen(
                                state = state,
                                navController = navController,
                                onEvent = viewModel::onUserEvent
                            )
                        }
                    }
                }
            }
        }
    }
}
