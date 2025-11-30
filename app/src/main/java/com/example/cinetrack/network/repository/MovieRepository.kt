package com.example.cinetrack.network.repository

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
}