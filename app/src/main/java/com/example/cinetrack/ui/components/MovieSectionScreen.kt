package com.example.cinetrack.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cinetrack.GridMoviePosterCard
import com.example.cinetrack.Movie
import com.example.cinetrack.MovieSortOption

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieSectionScreen(
    title: String,
    movies: List<Movie>,
    onMovieClick: (Int) -> Unit,
    onBackClick: () -> Unit,
    onRefreshClick: (() -> Unit)? = null
) {
    var sortOption by remember { mutableStateOf(MovieSortOption.DEFAULT) }

    val sortedMovies = remember(movies, sortOption) {
        when (sortOption) {
            MovieSortOption.DEFAULT -> movies

            MovieSortOption.TITLE_ASC -> movies.sortedBy { it.title.lowercase() }

            MovieSortOption.RATING_DESC -> movies.sortedByDescending { movie ->
                movie.rating
            }

            MovieSortOption.DATE_DESC -> movies.sortedByDescending { movie ->
                movie.year
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Geri"
                        )
                    }
                },
                actions = {
                    if (onRefreshClick != null) {
                        IconButton(onClick = onRefreshClick) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Önerileri yenile"
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            SortChipsRow(
                current = sortOption, onChange = { sortOption = it })

            if (sortedMovies.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                ) {
                    Text("Bu listede henüz film yok.")
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(sortedMovies) { movie ->
                        GridMoviePosterCard(
                            movie = movie, onClick = { onMovieClick(movie.id) })
                    }
                }
            }
        }
    }
}

@Composable
private fun SortChipsRow(
    current: MovieSortOption, onChange: (MovieSortOption) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SortChip(
            label = "Varsayılan",
            option = MovieSortOption.DEFAULT,
            current = current,
            onChange = onChange
        )

        SortChip(
            label = "İsme göre",
            option = MovieSortOption.TITLE_ASC,
            current = current,
            onChange = onChange
        )

        SortChip(
            label = "Puana göre",
            option = MovieSortOption.RATING_DESC,
            current = current,
            onChange = onChange
        )

        SortChip(
            label = "En yeni",
            option = MovieSortOption.DATE_DESC,
            current = current,
            onChange = onChange
        )
    }
}

@Composable
private fun SortChip(
    label: String,
    option: MovieSortOption,
    current: MovieSortOption,
    onChange: (MovieSortOption) -> Unit
) {
    FilterChip(selected = current == option, onClick = { onChange(option) }, label = {
        Text(
            label, maxLines = 1
        )
    })
}
