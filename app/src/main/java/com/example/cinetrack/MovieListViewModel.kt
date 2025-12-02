package com.example.cinetrack

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinetrack.data.local.AppDatabase
import com.example.cinetrack.data.local.TrackedMovieDao
import com.example.cinetrack.data.local.toMovie
import com.example.cinetrack.data.local.toTrackedEntity
import com.example.cinetrack.repository.MovieRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MovieListViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = MovieRepository(apiKey = BuildConfig.TMDB_API_KEY)

    private var searchJob: Job? = null

    private val trackedMovieDao: TrackedMovieDao =
        AppDatabase.getInstance(getApplication()).trackedMovieDao()

    var uiState by mutableStateOf(MovieListUiState())
        private set

    var searchUiState by mutableStateOf(SearchUiState())
        private set

    init {
        loadPopularMovies()
        loadTrackedMovies()
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

    private fun loadTrackedMovies() {
        viewModelScope.launch {
            val favorites = trackedMovieDao.getFavorites()
            val watchlist = trackedMovieDao.getByStatus(MovieStatus.WATCHLIST)
            val watched = trackedMovieDao.getByStatus(MovieStatus.WATCHED)

            uiState = uiState.copy(
                favoriteIDs = favorites.map { it.id }.toSet(),
                favoriteMovies = favorites.map { it.toMovie() },
                watchlistMovies = watchlist.map { it.toMovie() },
                watchedMovies = watched.map { it.toMovie()}
            )
        }
    }

    fun toggleFavorite(movie: Movie) {
        viewModelScope.launch {
            val existing = trackedMovieDao.getById(movie.id)
            val currentlyFavorite = existing?.isFavorite == true

            val updated = (existing ?: movie.toTrackedEntity())
                .copy(isFavorite = !currentlyFavorite)

            trackedMovieDao.upsert(updated)
            loadTrackedMovies()
        }
    }

    fun updateSearchQuery(newQuery: String) {
        searchUiState = searchUiState.copy(query = newQuery)

        searchJob?.cancel()

        if (newQuery.isBlank()) {
            searchUiState = searchUiState.copy(
                isLoading = false,
                results = emptyList(),
                errorMessage = null
            )
            return
        }

        searchJob = viewModelScope.launch {
            delay(400)
            performSearchInternal(newQuery)
        }
    }

     fun performSearch() {
        // Enter'a basınca çağıracağımız hızlı arama
        val query = searchUiState.query.trim()
        searchJob = viewModelScope.launch {
            performSearchInternal(query)
        }
    }

    private suspend fun performSearchInternal(rawQuery: String) {
        val query = rawQuery.trim()
        if (query.isBlank()) {
            searchUiState = searchUiState.copy(
                isLoading = false,
                results = emptyList(),
                errorMessage = null
            )
            return
        }

        searchUiState = searchUiState.copy(
            isLoading = true,
            errorMessage = null
        )

        try {
            val movies = repository.searchMovies(query)
            searchUiState = searchUiState.copy(
                isLoading = false,
                results = movies,
                errorMessage = null
            )
        } catch (e: Exception) {
            searchUiState = searchUiState.copy(
                isLoading = false,
                errorMessage = e.localizedMessage ?: "Arama sırasında bir hata oluştu"
            )
        }
    }

    fun setMovieStatus(movie: Movie, status: MovieStatus) {
        viewModelScope.launch {
            val existing = trackedMovieDao.getById(movie.id)
            val updated = (existing ?: movie.toTrackedEntity())
                .copy(status = status)

            trackedMovieDao.upsert(updated)
            loadTrackedMovies()
        }
    }

    fun clearMovieStatus(movie: Movie) {
        setMovieStatus(movie, MovieStatus.NONE)
    }
}