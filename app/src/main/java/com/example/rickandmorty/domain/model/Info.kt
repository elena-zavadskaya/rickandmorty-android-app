package com.example.rickandmorty.domain.model

import androidx.annotation.Keep

@Keep
data class Info(
    val count: Int,
    val pages: Int,
    val next: String?,
    val prev: String?
)