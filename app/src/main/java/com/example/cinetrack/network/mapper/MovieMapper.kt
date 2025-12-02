package com.example.cinetrack.network.mapper

import com.example.cinetrack.Movie
import com.example.cinetrack.network.model.MovieDto

private const val TMDB_IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"

fun MovieDto.toMovie(): Movie {
    val year = this.release_date?.take(4)?.toIntOrNull() ?: 0
    val posterUrl = if (!poster_path.isNullOrEmpty()) {
        TMDB_IMAGE_BASE_URL + poster_path
    } else {
        "https://via.placeholder.com/300x450.png?text=${title ?: original_title ?: "No+Title"}"
    }

    val displayTitle = when {
        !title.isNullOrBlank() -> title
        !original_title.isNullOrBlank() -> original_title
        else -> "Bilinmeyen Film"
    }

    return Movie(
        id = id,
        title = displayTitle,
        rating = vote_average,
        year = year,
        posterUrl = posterUrl
    )
}