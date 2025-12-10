package com.example.cinetrack.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.PlaylistAdd
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cinetrack.Movie
import com.example.cinetrack.MovieListUiState

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ProfileScreen(
    uiState: MovieListUiState,
    onBackClick: () -> Unit
) {
    val watched = uiState.watchedMovies
    val watchlist = uiState.watchlistMovies
    val favorites = uiState.favoriteMovies

    val watchedCount = watched.size
    val watchlistCount = watchlist.size
    val favoriteCount = favorites.size

    val averageRating = if (watched.isNotEmpty()) {
        watched.map { it.rating }.average()
    } else null

    val mostActiveYear = watched
        .groupBy { it.year }
        .maxByOrNull { it.value.size }
        ?.key

    val topRated = watched
        .sortedByDescending { it.rating }
        .take(3)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profil & İstatistikler") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Geri"
                        )
                    }
                }

            )
        }
    ) { innerPadding ->
        if (watchedCount == 0 && watchlistCount == 0 && favoriteCount == 0) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Henüz hiçbir film eklememişsin. \n Favorilerin, izleme listen veya izlediklerini güncelleyerek bu kısmı kendine göre kişiselleştirebilirsin.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Merhaba!",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = "CineTrack sinema maceranda seninle. İşte bugüne kadarki özetin:",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        StatCard(
                            title = "İzlediklerin",
                            value = watchedCount.toString(),
                            icon = Icons.Filled.Tv
                        )
                        StatCard(
                            title = "İzleme Listen",
                                                        value = watchlistCount.toString(),
                            icon = Icons.AutoMirrored.Filled.PlaylistAdd
                        )
                        StatCard(
                            title = "Favorilerin",
                            value = favoriteCount.toString(),
                            icon = Icons.Filled.Favorite
                        )
                    }
                }

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "İzleme İstatistiklerin",
                                style = MaterialTheme.typography.titleMedium
                            )

                            if (averageRating != null) {
                                Text(
                                    text = "İzlediklerinin ortalama puanı: ${
                                        "%.1f".format(
                                            averageRating
                                        )
                                    }",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }

                            if (mostActiveYear != null) {
                                Text(
                                    text = "En çok film izlediğin yıl: $mostActiveYear\nSen bu yılların insanısın!",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }

                            Text(
                                text = "Toplam izlediğin film sayısı: $watchedCount\nDevam et sayısız film seni bekliyor.",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }

                if (topRated.isNotEmpty()) {
                    item {
                        Text(
                            text = "En yüksek puana sahip izlediklerin",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    items(topRated) { movie ->
                        TopRatedMovieRow(movie = movie)
                    }
                }
            }
        }
    }
}

@Composable
private fun StatCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun TopRatedMovieRow(movie: Movie) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = movie.title,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "${movie.year} • ⭐ ${"%.1f".format(movie.rating)}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}