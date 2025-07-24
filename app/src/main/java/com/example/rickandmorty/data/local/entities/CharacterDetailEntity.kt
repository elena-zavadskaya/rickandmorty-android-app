package com.example.rickandmorty.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.rickandmorty.domain.model.CharacterDetail
import com.example.rickandmorty.domain.model.Location

@Entity(tableName = "character_details")
data class CharacterDetailEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val image: String,
    val originName: String,
    val locationName: String
)

fun CharacterDetailEntity.toCharacterDetail() = CharacterDetail(
    id = id,
    name = name,
    status = status,
    species = species,
    type = type,
    gender = gender,
    origin = Location(originName, ""),
    location = Location(locationName, ""),
    image = image,
    url = ""
)

fun CharacterDetail.toEntity() = CharacterDetailEntity(
    id = id,
    name = name,
    status = status,
    species = species,
    type = type,
    gender = gender,
    image = image,
    originName = origin.name,
    locationName = location.name
)

