package com.example.cinetrack

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinetrack.data.local.AppDatabase
import com.example.cinetrack.data.local.toFavoriteEntity
import com.example.cinetrack.data.local.toMovie
import com.example.cinetrack.repository.MovieRepository
import kotlinx.coroutines.launch


class MovieListViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = MovieRepository(apiKey = BuildConfig.TMDB_API_KEY)

    private val favoriteDao =
        AppDatabase.getInstance(getApplication()).favoriteMovieDao()

    var uiState by mutableStateOf(MovieListUiState())
        private set

    init {
        loadPopularMovies()
        loadFavorites()
    }

    fun loadPopularMovies(){
        uiState = uiState.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {
            try {
                val movies = repository.getPopularMovies()
                uiState = uiState.copy(
                    isLoading = false,
                    movies = movies,
                    errorMessage = null
                )
            } catch (e: Exception) {
                uiState = uiState.copy(
                    isLoading = false,
                    errorMessage = e.localizedMessage ?: "Bir hata oluştu."
                )
            }
        }
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            val favorites = favoriteDao.getAllFavorites()
            uiState = uiState.copy(
                favoriteIDs = favorites.map { it.id }.toSet(),
                favoriteMovies = favorites.map { it.toMovie() }
            )
        }
    }

    fun toggleFavorite(movie: Movie) {
        viewModelScope.launch {
            val currentlyFavorite = favoriteDao.isFavorite(movie.id)
            if (currentlyFavorite) {
                favoriteDao.deleteFavorite(movie.toFavoriteEntity())
            } else {
                favoriteDao.insertFavorite(movie.toFavoriteEntity())
            }
            loadFavorites()
        }
    }
}