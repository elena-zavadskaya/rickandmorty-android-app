package com.example.rickandmorty.data.repository

import com.example.rickandmorty.data.api.RickAndMortyApi
import com.example.rickandmorty.data.local.dao.CharacterDao
import com.example.rickandmorty.data.local.dao.CharacterDetailDao
import com.example.rickandmorty.data.local.entities.toCharacterDetail
import com.example.rickandmorty.data.local.entities.toEntity
import com.example.rickandmorty.domain.model.CharacterDetail
import com.example.rickandmorty.domain.model.CharacterItem
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

    suspend fun getCharacterDetail(id: Int): CharacterDetail = withContext(Dispatchers.IO) {
        val cached = characterDetailDao.getCharacterDetailById(id)
        if (cached != null) {
            return@withContext cached.toCharacterDetail()
        }

        val detail = api.getCharacterById(id)
        characterDetailDao.insert(detail.toEntity())
        detail
    }

    suspend fun filterCharacters(
        page: Int = 1,
        name: String? = null,
        status: String? = null,
        species: String? = null,
        type: String? = null,
        gender: String? = null
    ): LoadResult = withContext(Dispatchers.IO) {
        try {
            if (connectivity.isConnected()) {
                val response = api.getCharacters(
                    page = page,
                    name = name?.takeIf { it.isNotEmpty() },
                    status = status?.takeIf { it.isNotEmpty() },
                    species = species?.takeIf { it.isNotEmpty() },
                    type = type?.takeIf { it.isNotEmpty() },
                    gender = gender?.takeIf { it.isNotEmpty() }
                )

                LoadResult(
                    characters = response.results,
                    totalPages = response.info.pages
                )
            } else {
                val filtered = characterDao.filterCharacters(
                    name = name,
                    status = status,
                    species = species,
                    type = type,
                    gender = gender
                )
                LoadResult(
                    characters = filtered.map { it.toCharacterItem() },
                    totalPages = 1
                )
            }
        } catch (e: HttpException) {
            if (e.code() == 404) LoadResult(emptyList(), 0)
            else throw e
        } catch (e: Exception) {
            val filtered = characterDao.filterCharacters(
                name = name,
                status = status,
                species = species,
                type = type,
                gender = gender
            )
            LoadResult(
                characters = filtered.map { it.toCharacterItem() },
                totalPages = 1
            )
        }
    }
}
