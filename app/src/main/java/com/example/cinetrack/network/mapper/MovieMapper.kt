package com.example.cinetrack.network.mapper

import com.example.cinetrack.Movie
import com.example.cinetrack.network.model.MovieDto

private const val TMDB_IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"

fun MovieDto.toMovie(): Movie {
    val year = this.release_date?.take(4)?.toIntOrNull() ?: 0
    val posterUrl = if (!poster_path.isNullOrEmpty()) {
        TMDB_IMAGE_BASE_URL + poster_path
    } else {
        "https://via.placeholder.com/300x450.png?text=${title}"
    }

    return Movie(
        id = id,
        title = title,
        rating = vote_average,
        year = year,
        posterUrl = posterUrl
    )
}