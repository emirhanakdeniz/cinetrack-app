package com.example.cinetrack

data class Movie(
    val id: Int,
    val title: String,
    val rating: Double,
    val year: Int
)

val sampleMovies = listOf(
    Movie(1, "Inception", 8.8, 2010),
    Movie(2, "Interstellar", 8.6, 2014),
    Movie(3, "The Dark Knight", 9.0, 2008),
    Movie(4, "The Matrix", 8.7, 1999),
    Movie(5, "Fight Club", 8.8, 1999),
)

