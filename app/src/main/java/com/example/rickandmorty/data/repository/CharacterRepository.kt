package com.example.rickandmorty.data.repository

import com.example.rickandmorty.data.api.RickAndMortyApi
import com.example.rickandmorty.data.local.CharacterDao
import com.example.rickandmorty.data.local.toCharacterEntity
import com.example.rickandmorty.data.model.CharacterDetail
import com.example.rickandmorty.data.model.CharacterItem
import com.example.rickandmorty.data.network.Connectivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CharacterRepository @Inject constructor(
    private val api: RickAndMortyApi,
    private val characterDao: CharacterDao,
    private val connectivity: Connectivity
) {
    data class LoadResult(
        val characters: List<CharacterItem>,
        val totalPages: Int
    )

    suspend fun loadCharacters(page: Int): LoadResult = withContext(Dispatchers.IO) {
        try {
            if (connectivity.isConnected()) {
                val response = api.getCharacters(page)
                val totalPages = response.info.pages

                val entities = response.results.map { it.toCharacterEntity(page) }
                characterDao.deletePage(page)
                characterDao.insertAll(entities)

                return@withContext LoadResult(
                    characters = entities.map { it.toCharacterItem() },
                    totalPages = totalPages
                )
            } else {
                val entities = characterDao.getCharactersByPage(page)
                return@withContext LoadResult(
                    characters = entities.map { it.toCharacterItem() },
                    totalPages = characterDao.getMaxPage() ?: 1
                )
            }
        } catch (e: Exception) {
            val entities = characterDao.getCharactersByPage(page)
            return@withContext LoadResult(
                characters = entities.map { it.toCharacterItem() },
                totalPages = characterDao.getMaxPage() ?: 1
            )
        }
    }

    suspend fun getCharacterDetail(id: Int): CharacterDetail {
        return withContext(Dispatchers.IO) {
            if (connectivity.isConnected()) {
                api.getCharacterById(id)
            } else {
                throw Exception("No internet connection")
            }
        }
    }
}