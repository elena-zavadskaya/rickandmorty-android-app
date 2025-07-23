package com.example.rickandmorty.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.rickandmorty.data.model.CharacterItem
import com.example.rickandmorty.data.model.Location

@Entity(tableName = "characters")
data class CharacterEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val image: String,
    val page: Int
) {
    fun toCharacterItem() = CharacterItem(
        id = id,
        name = name,
        status = status,
        species = species,
        type = type,
        gender = gender,
        origin = Location("", ""),
        location = Location("", ""),
        image = image,
        episode = emptyList(),
        url = "",
        created = "",
        page = page
    )
}

fun CharacterItem.toCharacterEntity(page: Int) = CharacterEntity(
    id = id,
    name = name,
    status = status,
    species = species,
    type = type,
    gender = gender,
    image = image,
    page = page
)