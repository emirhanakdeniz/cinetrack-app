package com.example.cinetrack.network.model
data class MovieDto(
    val id: Int,
    val title:String?,
    val original_title: String?,
    val vote_average: Double,
    val release_date: String?,
    val poster_path: String?
)
