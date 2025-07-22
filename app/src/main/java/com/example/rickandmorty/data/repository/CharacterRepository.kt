package com.example.rickandmorty.data.repository

import com.example.rickandmorty.data.api.RickAndMortyApi
import com.example.rickandmorty.data.local.CharacterDao
import com.example.rickandmorty.data.local.CharacterEntity
import com.example.rickandmorty.data.local.toCharacterEntity
import com.example.rickandmorty.data.model.CharacterItem
import com.example.rickandmorty.data.model.CharacterResponse
import com.example.rickandmorty.data.model.Info
import com.example.rickandmorty.data.network.Connectivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CharacterRepository @Inject constructor(
    private val api: RickAndMortyApi,
    private val characterDao: CharacterDao,
    val connectivity: Connectivity
) {
    private var cachedCharacters: List<CharacterItem>? = null

    suspend fun getAllCharacters(): List<CharacterItem> {
        return withContext(Dispatchers.IO) {
            cachedCharacters = null

            val allCharacters = mutableListOf<CharacterItem>()
            var currentPage = 1
            var totalPages = 1

            try {
                do {
                    val response = api.getCharacters(currentPage)
                    allCharacters.addAll(response.results)

                    totalPages = response.info.pages
                    currentPage++

                } while (currentPage <= totalPages)

                cachedCharacters = allCharacters
                allCharacters
            } catch (e: Exception) {
                cachedCharacters ?: emptyList()
            }
        }
    }

    fun getCharactersStream(): Flow<List<CharacterItem>> {
        return characterDao.getAllCharacters().map { entities ->
            entities.map { it.toCharacterItem() }
        }
    }

    suspend fun refreshCharacters() {
        if (!connectivity.isConnected()) {
            return
        }

        val allCharacters = mutableListOf<CharacterEntity>()
        var currentPage = 1
        var totalPages = 1

        try {
            do {
                val response = api.getCharacters(currentPage)
                allCharacters.addAll(response.results.map { it.toCharacterEntity() })
                totalPages = response.info.pages
                currentPage++
            } while (currentPage <= totalPages)

            characterDao.clearAll()
            characterDao.insertAll(allCharacters)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun loadInitialData() {
        try {
            if (connectivity.isConnected()) {
                refreshCharacters()
            }
        } catch (e: Exception) {
        }
    }

    suspend fun getCharactersPage(page: Int): CharacterResponse {
        return withContext(Dispatchers.IO) {
            try {
                api.getCharacters(page)
            } catch (e: Exception) {
                CharacterResponse(
                    info = Info(0, 0, null, null),
                    results = emptyList()
                )
            }
        }
    }
}