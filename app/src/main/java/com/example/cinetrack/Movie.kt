package com.example.cinetrack

data class Movie(
    val id: Int,
    val title: String,
    val rating: Double,
    val year: Int,
    val posterUrl: String
)

val sampleMovies = listOf(
    Movie(
        1,
        "Inception",
        8.8,
        2010,
        "https://via.placeholder.com/300x450.png?text=Inception"
    ),
    Movie(
        2,
        "Interstellar",
        8.6,
        2014,
        "https://via.placeholder.com/300x450.png?text=Interstellar"
    ),
    Movie(
        3,
        "The Dark Knight",
        9.0,
        2008,
        "https://via.placeholder.com/300x450.png?text=The Dark Knight"
        ),
    Movie(
        4,
        "The Matrix",
        8.7,
        1999,
        "https://via.placeholder.com/300x450.png?text=The Matrix"
        ),
    Movie(
        5,
        "Fight Club",
        8.8,
        1999,
        "https://via.placeholder.com/300x450.png?text=Fight Club"
        ),
)

