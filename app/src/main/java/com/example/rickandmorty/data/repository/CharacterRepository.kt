package com.example.rickandmorty.data.repository

import com.example.rickandmorty.data.api.RickAndMortyApi
import com.example.rickandmorty.data.local.CharacterDao
import com.example.rickandmorty.data.local.CharacterDetailDao
import com.example.rickandmorty.data.local.toCharacterDetail
import com.example.rickandmorty.data.local.toCharacterEntity
import com.example.rickandmorty.data.local.toEntity
import com.example.rickandmorty.data.model.CharacterDetail
import com.example.rickandmorty.data.model.CharacterItem
import com.example.rickandmorty.data.network.Connectivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject

class CharacterRepository @Inject constructor(
    private val api: RickAndMortyApi,
    private val characterDao: CharacterDao,
    private val characterDetailDao: CharacterDetailDao,
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

                LoadResult(
                    characters = entities.map { it.toCharacterItem() },
                    totalPages = totalPages
                )
            } else {
                val entities = characterDao.getCharactersByPage(page)
                LoadResult(
                    characters = entities.map { it.toCharacterItem() },
                    totalPages = characterDao.getMaxPage() ?: 1
                )
            }
        } catch (e: Exception) {
            val entities = characterDao.getCharactersByPage(page)
            LoadResult(
                characters = entities.map { it.toCharacterItem() },
                totalPages = characterDao.getMaxPage() ?: 1
            )
        }
    }

    suspend fun getCharacterDetail(id: Int): CharacterDetail = withContext(Dispatchers.IO) {
        val cached = characterDetailDao.getCharacterDetailById(id)
        if (cached != null) {
            return@withContext cached.toCharacterDetail()
        }

        val detail = api.getCharacterById(id)
        characterDetailDao.insert(detail.toEntity())
        detail
    }

    suspend fun searchCharactersByName(name: String, page: Int = 1): LoadResult {
        return withContext(Dispatchers.IO) {
            try {
                if (connectivity.isConnected()) {
                    val response = api.getCharacters(page = page, name = name)
                    LoadResult(
                        characters = response.results,
                        totalPages = response.info.pages
                    )
                } else {
                    val entities = characterDao.searchCharactersByName(name)
                    LoadResult(
                        characters = entities.map { it.toCharacterItem() },
                        totalPages = 1
                    )
                }
            } catch (e: Exception) {
                if (e is HttpException && e.code() == 404) {
                    LoadResult(emptyList(), 0)
                } else {
                    val entities = characterDao.searchCharactersByName(name)
                    LoadResult(
                        characters = entities.map { it.toCharacterItem() },
                        totalPages = 1
                    )
                }
            }
        }
    }
}
