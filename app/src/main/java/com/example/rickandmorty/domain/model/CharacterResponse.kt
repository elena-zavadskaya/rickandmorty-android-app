package com.example.rickandmorty.domain.model

import androidx.annotation.Keep

@Keep
data class CharacterResponse(
    val info: Info,
    val results: List<CharacterItem>
)