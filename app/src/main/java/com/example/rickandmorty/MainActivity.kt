package com.example.rickandmorty

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.rickandmorty.navigation.NavRoutes
import com.example.rickandmorty.presentation.home.ScrollState
import com.example.rickandmorty.screens.character_details.CharacterDetailsScreen
import com.example.rickandmorty.screens.filters.FiltersScreen
import com.example.rickandmorty.screens.home.HomeScreen
import com.example.rickandmorty.ui.theme.RickMortyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RickMortyTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "home_root"
                ) {
                    navigation(
                        startDestination = NavRoutes.HOME,
                        route = "home_root"
                    ) {
                        composable(
                            route = NavRoutes.HOME,
                            arguments = listOf(
                                navArgument("searchQuery") {
                                    type = NavType.StringType
                                    defaultValue = ""
                                    nullable = true
                                },
                                navArgument("scrollIndex") {
                                    type = NavType.IntType
                                    defaultValue = 0
                                },
                                navArgument("scrollOffset") {
                                    type = NavType.IntType
                                    defaultValue = 0
                                }
                            )
                        ) { backStackEntry ->
                            val searchQuery = backStackEntry.arguments?.getString("searchQuery") ?: ""
                            val scrollIndex = backStackEntry.arguments?.getInt("scrollIndex") ?: 0
                            val scrollOffset = backStackEntry.arguments?.getInt("scrollOffset") ?: 0

                            HomeScreen(
                                navController = navController,
                                initialSearchQuery = searchQuery,
                                initialScrollState = ScrollState(scrollIndex, scrollOffset)
                            )
                        }
                    }
                    composable(NavRoutes.FILTERS) { FiltersScreen() }
                    composable(
                        route = NavRoutes.CHARACTER_DETAILS,
                        arguments = listOf(navArgument("characterId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val characterId = backStackEntry.arguments?.getInt("characterId") ?: 0
                        CharacterDetailsScreen(navController = navController)
                    }
                }
            }
        }
    }
}