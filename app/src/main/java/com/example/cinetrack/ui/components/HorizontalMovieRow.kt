package com.example.cinetrack.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.cinetrack.Movie
import com.example.cinetrack.MoviePosterCard

@Composable
fun HorizontalMovieRow(
    movies: List<Movie>, onMovieClick: (Int) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(movies) { movie ->
            MoviePosterCard(
                movie = movie, onClick = { onMovieClick(movie.id) })
        }
    }
}
