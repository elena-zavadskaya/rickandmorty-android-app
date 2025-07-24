package com.example.rickandmorty.data.api

import com.example.rickandmorty.data.model.CharacterDetail
import com.example.rickandmorty.data.model.CharacterResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RickAndMortyApi {
    @GET("character")
    suspend fun getCharacters(
        @Query("page") page: Int,
        @Query("name") name: String? = null,
        @Query("status") status: String? = null,
        @Query("species") species: String? = null,
        @Query("type") type: String? = null,
        @Query("gender") gender: String? = null
    ): CharacterResponse

    @GET("character/{id}")
    suspend fun getCharacterById(
        @Path("id") id: Int
    ): CharacterDetail
}