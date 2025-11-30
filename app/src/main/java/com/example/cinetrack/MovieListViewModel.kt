package com.example.cinetrack

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinetrack.repository.MovieRepository
import kotlinx.coroutines.launch
class MovieListViewModel : ViewModel() {

    private val repository = MovieRepository(apiKey = BuildConfig.TMDB_API_KEY)

    var uiState by mutableStateOf(MovieListUiState())
        private set

    init {
        loadPopularMovies()
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
}