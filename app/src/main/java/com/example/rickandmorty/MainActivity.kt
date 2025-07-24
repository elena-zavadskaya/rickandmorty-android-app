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
import com.example.rickandmorty.domain.model.CharacterFilters
import com.example.rickandmorty.presentation.navigation.NavRoutes
import com.example.rickandmorty.presentation.viewmodel.ScrollState
import com.example.rickandmorty.presentation.screens.CharacterDetailsScreen
import com.example.rickandmorty.presentation.screens.FiltersScreen
import com.example.rickandmorty.presentation.screens.HomeScreen
import com.example.rickandmorty.presentation.ui.RickMortyTheme
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
                            route = NavRoutes.HOME +
                                    "?searchQuery={searchQuery}" +
                                    "&status={status}" +
                                    "&species={species}" +
                                    "&type={type}" +
                                    "&gender={gender}",
                            arguments = listOf(
                                navArgument("searchQuery") {
                                    type = NavType.StringType
                                    defaultValue = ""
                                    nullable = true
                                },
                                navArgument("status") {
                                    type = NavType.StringType
                                    defaultValue = null
                                    nullable = true
                                },
                                navArgument("species") {
                                    type = NavType.StringType
                                    defaultValue = null
                                    nullable = true
                                },
                                navArgument("type") {
                                    type = NavType.StringType
                                    defaultValue = null
                                    nullable = true
                                },
                                navArgument("gender") {
                                    type = NavType.StringType
                                    defaultValue = null
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

                            val filters = CharacterFilters(
                                name = backStackEntry.arguments?.getString("searchQuery"),
                                status = backStackEntry.arguments?.getString("status"),
                                species = backStackEntry.arguments?.getString("species"),
                                type = backStackEntry.arguments?.getString("type"),
                                gender = backStackEntry.arguments?.getString("gender")
                            )

                            HomeScreen(
                                navController = navController,
                                initialSearchQuery = searchQuery,
                                initialScrollState = ScrollState(scrollIndex, scrollOffset),
                                initialFilters = filters
                            )
                        }

                    }
                    composable(NavRoutes.FILTERS) {
                        FiltersScreen(navController = navController)
                    }
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