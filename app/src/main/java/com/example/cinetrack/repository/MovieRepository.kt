package com.example.cinetrack.repository

import com.example.cinetrack.Movie
import com.example.cinetrack.network.MovieApiClient
import com.example.cinetrack.network.mapper.toMovie

class MovieRepository (
    private val apiKey: String
) {
    suspend fun getPopularMovies(): List<Movie> {
        val response = MovieApiClient.apiService.getPopularMovies(apiKey)
        return response.results.map{ it.toMovie() }
    }

    suspend fun searchMovies(query: String): List<Movie> {
        if(query.isBlank()) return emptyList()

        val response = MovieApiClient.apiService.searchMovies(
            apiKey = apiKey,
            query = query
        )
        return response.results.map { it.toMovie() }
    }
}