package com.example.rickandmorty.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.rickandmorty.data.local.entities.CharacterEntity

@Dao
interface CharacterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(characters: List<CharacterEntity>)

    @Query("SELECT * FROM characters WHERE page = :page")
    suspend fun getCharactersByPage(page: Int): List<CharacterEntity>

    @Query("DELETE FROM characters WHERE page = :page")
    suspend fun deletePage(page: Int)

    @Query("SELECT MAX(page) FROM characters")
    suspend fun getMaxPage(): Int?

    @Query("SELECT * FROM characters WHERE name LIKE '%' || :query || '%'")
    suspend fun searchCharactersByName(query: String): List<CharacterEntity>

    @Query("""
        SELECT * FROM characters 
        WHERE (:name IS NULL OR :name = '' OR name LIKE '%' || :name || '%' COLLATE NOCASE)
          AND (:status IS NULL OR :status = '' OR status = :status COLLATE NOCASE)
          AND (:species IS NULL OR :species = '' OR species = :species COLLATE NOCASE)
          AND (:type IS NULL OR :type = '' OR type = :type COLLATE NOCASE)
          AND (:gender IS NULL OR :gender = '' OR gender = :gender COLLATE NOCASE)
    """)
    suspend fun filterCharacters(
        name: String?,
        status: String?,
        species: String?,
        type: String?,
        gender: String?
    ): List<CharacterEntity>
}