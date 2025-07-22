package com.example.rickandmorty.data.model

import androidx.annotation.Keep

@Keep
data class Location(
    val name: String,
    val url: String
)