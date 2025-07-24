package com.example.rickandmorty.domain.model

import androidx.annotation.Keep

@Keep
data class CharacterItem(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val origin: Location,
    val location: Location,
    val image: String,
    val episode: List<String>,
    val url: String,
    val created: String,
    val page: Int
)