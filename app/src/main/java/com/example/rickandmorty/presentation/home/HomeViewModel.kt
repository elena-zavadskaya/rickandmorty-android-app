package com.example.rickandmorty.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.data.model.CharacterItem
import com.example.rickandmorty.data.repository.CharacterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: CharacterRepository
) : ViewModel() {

    val characters: Flow<List<CharacterItem>> = repository.getCharactersStream()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private val _networkError = MutableStateFlow(false)
    val networkError: StateFlow<Boolean> = _networkError.asStateFlow()

    init {
        loadInitialData()
    }

    fun refreshCharacters() {
        viewModelScope.launch {
            _isRefreshing.value = true
            _networkError.value = false
            try {
                repository.refreshCharacters()
            } catch (e: Exception) {
                _networkError.value = true
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _isLoading.value = true
            _networkError.value = false
            try {
                repository.loadInitialData()
            } catch (e: Exception) {
                _networkError.value = true
            } finally {
                _isLoading.value = false
            }
        }
    }
}