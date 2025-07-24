package com.example.rickandmorty.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.rickandmorty.data.local.entities.CharacterDetailEntity

@Dao
interface CharacterDetailDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(detail: CharacterDetailEntity)

    @Query("SELECT * FROM character_details WHERE id = :id")
    suspend fun getCharacterDetailById(id: Int): CharacterDetailEntity?
}