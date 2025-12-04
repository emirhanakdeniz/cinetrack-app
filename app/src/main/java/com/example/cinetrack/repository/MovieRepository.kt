package com.example.cinetrack.repository

import com.example.cinetrack.Movie
import com.example.cinetrack.network.MovieApiClient
import com.example.cinetrack.network.mapper.toMovie

class MovieRepository(
    private val apiKey: String
) {
    suspend fun getPopularMovies(): List<Movie> {
        val response = MovieApiClient.apiService.getPopularMovies(apiKey)
        return response.results.map { it.toMovie() }
    }

    suspend fun searchMovies(query: String): List<Movie> {
        if (query.isBlank()) return emptyList()

        val response = MovieApiClient.apiService.searchMovies(
            apiKey = apiKey, query = query
        )
        return response.results.map { it.toMovie() }
    }

    suspend fun getRecommendationsForMovies(seedMovieIds: List<Int>): List<Movie> {
        if (seedMovieIds.isEmpty()) return emptyList()

        val allRecommended = mutableListOf<Movie>()

        for (id in seedMovieIds) {
            try {
                val response = MovieApiClient.apiService.getRecommendations(
                    movieId = id, apiKey = apiKey
                )
                val movies = response.results.map { it.toMovie() }
                allRecommended += movies
            } catch (e: Exception) {
            }
        }

        val unique = allRecommended.distinctBy { it.id }

        return unique.take(20)

    }
}