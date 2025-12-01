package com.example.cinetrack

data class MovieListUiState(
    val isLoading: Boolean = false,
    val movies: List<Movie> = emptyList(),
    val errorMessage: String? = null,
    val favoriteIDs: Set<Int> = emptySet(),
    val favoriteMovies: List<Movie> = emptyList()
)
