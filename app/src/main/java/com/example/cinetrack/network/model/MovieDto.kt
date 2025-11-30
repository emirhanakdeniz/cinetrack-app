package com.example.cinetrack.network.model
data class MovieDto(
    val id: Int,
    val title:String,
    val vote_average: Double,
    val relase_date: String?,
    val poster_path: String?
)
