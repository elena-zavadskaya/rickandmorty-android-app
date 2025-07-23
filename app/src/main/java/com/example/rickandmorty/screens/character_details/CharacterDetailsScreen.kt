package com.example.rickandmorty.screens.character_details

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun CharacterDetailsScreen(characterId: Int) {
    Text(text = "Детали персонажа ID: $characterId")
}