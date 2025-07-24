package com.example.rickandmorty.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.rickandmorty.data.local.dao.CharacterDao
import com.example.rickandmorty.data.local.dao.CharacterDetailDao
import com.example.rickandmorty.data.local.entities.CharacterDetailEntity
import com.example.rickandmorty.data.local.entities.CharacterEntity

@Database(
    entities = [CharacterEntity::class, CharacterDetailEntity::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun characterDao(): CharacterDao
    abstract fun characterDetailDao(): CharacterDetailDao
}