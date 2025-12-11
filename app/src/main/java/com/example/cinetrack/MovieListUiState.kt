package com.example.cinetrack

data class MovieListUiState(
    val isLoading: Boolean = false,
    val movies: List<Movie> = emptyList(),
    val errorMessage: String? = null,
    val favoriteIDs: Set<Int> = emptySet(),
    val favoriteMovies: List<Movie> = emptyList(),
    val watchlistMovies: List<Movie> = emptyList(),
    val watchedMovies: List<Movie> = emptyList(),
    val recommendedMovies: List<Movie> = emptyList(),
    val userName: String = "Sinema Sever"
)
