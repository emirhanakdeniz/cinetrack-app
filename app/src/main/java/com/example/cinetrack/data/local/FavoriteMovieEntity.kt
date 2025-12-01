package com.example.cinetrack.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.cinetrack.Movie
import com.example.cinetrack.MovieStatus

@Entity(tableName = "tracked_movies")
data class TrackedMovieEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val rating: Double,
    val year: Int,
    val posterUrl: String,
    val isFavorite: Boolean,
    val status: MovieStatus
)

// mappers
fun TrackedMovieEntity.toMovie(): Movie =
    Movie(
        id = id,
        title = title,
        rating = rating,
        year = year,
        posterUrl = posterUrl
    )

fun Movie.toTrackedEntity(
    isFavorite: Boolean = false,
    status: MovieStatus = MovieStatus.NONE
): TrackedMovieEntity =
    TrackedMovieEntity(
        id = id,
        title = title,
        rating = rating,
        year = year,
        posterUrl = posterUrl,
        isFavorite = isFavorite,
        status = status
    )