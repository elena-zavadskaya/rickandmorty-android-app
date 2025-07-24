package com.example.rickandmorty.presentation.home

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.data.model.CharacterFilters
import com.example.rickandmorty.data.model.CharacterItem
import com.example.rickandmorty.data.repository.CharacterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: CharacterRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _characters = MutableStateFlow<List<CharacterItem>>(emptyList())
    val characters: StateFlow<List<CharacterItem>> = _characters.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private val _isLoadingNextPage = MutableStateFlow(false)
    val isLoadingNextPage: StateFlow<Boolean> = _isLoadingNextPage.asStateFlow()

    private val _networkError = MutableStateFlow(false)
    val networkError: StateFlow<Boolean> = _networkError.asStateFlow()

    private val _canLoadMore = MutableStateFlow(true)
    val canLoadMore: StateFlow<Boolean> = _canLoadMore.asStateFlow()

    private val _scrollState = MutableStateFlow(
        ScrollState(
            savedStateHandle.get<Int>("scrollIndex") ?: 0,
            savedStateHandle.get<Int>("scrollOffset") ?: 0
        )
    )
    val scrollState: StateFlow<ScrollState> = _scrollState.asStateFlow()

    private val _filters = MutableStateFlow(
        CharacterFilters(
            name = savedStateHandle.get<String>("name"),
            status = savedStateHandle.get<String>("status"),
            species = savedStateHandle.get<String>("species"),
            type = savedStateHandle.get<String>("type"),
            gender = savedStateHandle.get<String>("gender")
        )
    )
    val filters: StateFlow<CharacterFilters> = _filters.asStateFlow()

    private var currentPage = 1
    private var totalPages = 1

    init {
        viewModelScope.launch {
            _filters.collect { filters ->
                savedStateHandle["name"] = filters.name
                savedStateHandle["status"] = filters.status
                savedStateHandle["species"] = filters.species
                savedStateHandle["type"] = filters.type
                savedStateHandle["gender"] = filters.gender
            }
        }

        viewModelScope.launch {
            _scrollState.collect { state ->
                savedStateHandle["scrollIndex"] = state.firstVisibleItemIndex
                savedStateHandle["scrollOffset"] = state.firstVisibleItemScrollOffset
            }
        }

        viewModelScope.launch {
            _isLoading.value = true
            loadPage(1)
        }
    }

    fun setFilters(filters: CharacterFilters) {
        _filters.value = filters
        currentPage = 1
        _characters.value = emptyList()
        refreshCharacters()
    }

    fun setSearchQuery(query: String?) {
        _filters.value = _filters.value.copy(name = query)
        refreshCharacters()
    }

    fun saveScrollPosition(firstVisibleItemIndex: Int, firstVisibleItemScrollOffset: Int) {
        _scrollState.value = ScrollState(firstVisibleItemIndex, firstVisibleItemScrollOffset)
    }

    fun refreshCharacters() {
        viewModelScope.launch {
            _isRefreshing.value = true
            _networkError.value = false
            currentPage = 1
            loadPage(currentPage)
        }
    }

    fun loadNextPage() {
        if (currentPage >= totalPages || _isLoadingNextPage.value) return

        viewModelScope.launch {
            _isLoadingNextPage.value = true
            currentPage++
            loadPage(currentPage)
        }
    }

    private suspend fun loadPage(page: Int) {
        Log.d("HomeViewModel", "loadPage($page) started")
        try {
            val filters = _filters.value
            val result = repository.filterCharacters(
                page = page,
                name = filters.name,
                status = filters.status,
                species = filters.species,
                type = filters.type,
                gender = filters.gender
            )

            _characters.value = if (page == 1) {
                result.characters
            } else {
                _characters.value + result.characters
            }

            totalPages = result.totalPages
            _canLoadMore.value = currentPage < totalPages
            _networkError.value = false
        } catch (e: Exception) {
            Log.e("HomeViewModel", "Error loading characters: ${e.message}")
            _networkError.value = true
        } finally {
            _isLoading.value = false
            _isLoadingNextPage.value = false
            _isRefreshing.value = false
        }
    }
}

data class ScrollState(
    val firstVisibleItemIndex: Int = 0,
    val firstVisibleItemScrollOffset: Int = 0
)