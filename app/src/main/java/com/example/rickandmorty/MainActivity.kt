package com.example.rickandmorty

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.rickandmorty.navigation.NavRoutes
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
                    startDestination = NavRoutes.HOME
                ) {
                    composable(NavRoutes.HOME) { HomeScreen(navController) }
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