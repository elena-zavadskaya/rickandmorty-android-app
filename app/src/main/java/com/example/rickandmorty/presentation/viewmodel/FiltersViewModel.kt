package com.example.rickandmorty.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class FiltersViewModel : ViewModel() {
    private var _name by mutableStateOf<String?>(null)
    private var _species by mutableStateOf<String?>(null)
    private var _type by mutableStateOf<String?>(null)
    private var _status by mutableStateOf<String?>(null)
    private var _gender by mutableStateOf<String?>(null)

    val name: String? get() = _name
    val species: String? get() = _species
    val type: String? get() = _type
    val status: String? get() = _status
    val gender: String? get() = _gender

    fun setName(value: String?) {
        _name = value?.takeIf { it.isNotEmpty() }
    }

    fun setSpecies(value: String?) {
        _species = value?.takeIf { it.isNotEmpty() }
    }

    fun setType(value: String?) {
        _type = value?.takeIf { it.isNotEmpty() }
    }

    fun setStatus(value: String?) {
        _status = value?.takeIf { it.isNotEmpty() }
    }

    fun setGender(value: String?) {
        _gender = value?.takeIf { it.isNotEmpty() }
    }

    fun resetAllFilters() {
        _name = null
        _species = null
        _type = null
        _status = null
        _gender = null
    }
}