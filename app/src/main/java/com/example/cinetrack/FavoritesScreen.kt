package com.example.cinetrack

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cinetrack.ui.components.EmptyState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    favoriteMovies: List<Movie>, onMovieClick: (Int) -> Unit, onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Favoriler") }, navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Geri"
                    )
                }
            })
        }) { innerPadding ->
        if (favoriteMovies.isEmpty()) {
            EmptyState(
                title = "Favoriler boş",
                message = "Henüz bir film eklememişsin gibi gözüküyor. Yeni filmlere göz atmaya ne dersin?"
            )
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(favoriteMovies.size) { index ->
                    val movie = favoriteMovies[index]
                    GridMoviePosterCard(
                        movie = movie, onClick = { onMovieClick(movie.id) })
                }
            }
        }
    }
}

