package com.example.rickandmorty

import android.app.Application
import com.example.rickandmorty.data.repository.CharacterRepository
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class RickAndMortyApplication : Application() {
    @Inject
    lateinit var repository: CharacterRepository

    override fun onCreate() {
        super.onCreate()

        CoroutineScope(Dispatchers.IO).launch {
            repository.loadInitialData()
        }
    }
}