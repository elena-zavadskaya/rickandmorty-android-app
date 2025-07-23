package com.example.rickandmorty.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.data.model.CharacterItem
import com.example.rickandmorty.data.repository.CharacterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: CharacterRepository
) : ViewModel() {

    private val _characters = MutableStateFlow<List<CharacterItem>>(emptyList())
    val characters: StateFlow<List<CharacterItem>> = _characters.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isLoadingNextPage = MutableStateFlow(false)
    val isLoadingNextPage: StateFlow<Boolean> = _isLoadingNextPage.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private val _networkError = MutableStateFlow(false)
    val networkError: StateFlow<Boolean> = _networkError.asStateFlow()

    private val _canLoadMore = MutableStateFlow(true)
    val canLoadMore: StateFlow<Boolean> = _canLoadMore.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private var currentPage = 1
    private var totalPages = 1
    private var lastSearchQuery: String = ""

    fun refreshCharacters() {
        viewModelScope.launch {
            _isRefreshing.value = true
            _networkError.value = false
            currentPage = 1
            loadPage(currentPage)
        }
    }

    fun loadNextPage() {
        if (currentPage >= totalPages) return

        viewModelScope.launch {
            _isLoadingNextPage.value = true
            currentPage++
            loadPage(currentPage)
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
        refreshCharacters()
    }

    private suspend fun loadPage(page: Int) {
        try {
            val result = if (_searchQuery.value.isEmpty()) {
                repository.loadCharacters(page)
            } else {
                repository.searchCharactersByName(_searchQuery.value, page)
            }

            _characters.value = if (page == 1) {
                result.characters
            } else {
                _characters.value + result.characters
            }

            totalPages = result.totalPages
            _canLoadMore.value = currentPage < totalPages
            _networkError.value = false
        } catch (e: Exception) {
            if (e is HttpException && e.code() == 404) {
                _characters.value = emptyList()
                _canLoadMore.value = false
            } else {
                _networkError.value = true
            }
        } finally {
            _isLoading.value = false
            _isLoadingNextPage.value = false
            _isRefreshing.value = false
        }
    }

    init {
        viewModelScope.launch {
            _isLoading.value = true
            loadPage(1)
        }
    }
}