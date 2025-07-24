package com.example.rickandmorty.domain.model

import androidx.annotation.Keep

@Keep
data class Location(
    val name: String,
    val url: String
)