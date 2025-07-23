package com.example.rickandmorty.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

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
}