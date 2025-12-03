package com.example.cinetrack

data class Movie(
    val id: Int,
    val title: String,
    val rating: Double,
    val year: Int,
    val posterUrl: String,
    val overview: String? = null,
    val originalLanguage: String? = null
)

val sampleMovies = listOf(
    Movie(
        1,
        "Inception",
        8.8,
        2010,
        "https://www.movieposters.com/cdn/shop/files/inception.mpw.123395_9e0000d1-bc7f-400a-b488-15fa9e60a10c.jpg?v=1762975399&width=1680"
    ),
    Movie(
        2,
        "Interstellar",
        8.6,
        2014,
        "https://www.movieposters.com/cdn/shop/files/interstellar-139399.jpg?v=1762974876&width=1680"
    ),
    Movie(
        3,
        "The Dark Knight",
        9.0,
        2008,
        "https://www.movieposters.com/cdn/shop/products/darkknight.building.mp.jpg?v=1762970009&width=1680"
    ),
    Movie(
        4,
        "The Matrix",
        8.7,
        1999,
        "https://www.movieposters.com/cdn/shop/products/ed4796ac6feff9d2a6115406f964c928_6b200bda-fe71-4900-ad7f-903cdda50dab.jpg?v=1762505090&width=1680"
    ),
    Movie(
        5,
        "Fight Club",
        8.8,
        1999,
        "https://www.movieposters.com/cdn/shop/files/FightClub.mpw.125100.jpg?v=1762965511&width=1680"
    ),
)

