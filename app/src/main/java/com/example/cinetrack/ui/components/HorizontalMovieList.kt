package com.example.cinetrack.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.cinetrack.Movie

@Composable
fun HorizontalMovieList(
    movies: List<Movie>, onMovieClick: (Int) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(movies) { movie ->
            MoviePosterItem(movie = movie, onClick = { onMovieClick(movie.id) })
        }
    }
}

@Composable
fun MoviePosterItem(
    movie: Movie, onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(120.dp)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = movie.posterUrl),
            contentDescription = movie.title,
            modifier = Modifier
                .height(180.dp)
                .fillMaxWidth()
        )
    }
}