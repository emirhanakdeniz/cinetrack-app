package com.example.cinetrack

data class SearchUiState(
    val query: String = "",
    val isLoading: Boolean = false,
    val results: List<Movie> = emptyList(),
    val errorMessage: String? = null
)
