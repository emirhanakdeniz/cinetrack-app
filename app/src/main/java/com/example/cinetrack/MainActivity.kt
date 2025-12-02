package com.example.cinetrack

//import androidx.compose.ui.tooling.preview.Preview
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.PlaylistAdd
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cinetrack.ui.theme.CineTrackTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CineTrackTheme{
                CineTrackApp()
            }
        }
    }
}

@Composable
fun CineTrackApp() {
        val navController = rememberNavController()
        val movieListViewModel: MovieListViewModel = viewModel()
        val uiState = movieListViewModel.uiState

        NavHost(
            navController = navController,
            startDestination = "movie_list"
        ) {
            composable("movie_list") {
                MovieListScreen(
                    uiState = uiState,
                    onMovieClick = { movieId ->
                        navController.navigate("movie_detail/$movieId")
                    },
                    onRetryClick = { movieListViewModel.loadPopularMovies() },
                    onNavigateToFavorites = { navController.navigate("favorites") },
                    onNavigateToSearch = { navController.navigate("search") },
                    onNavigateToWatchlist = { navController.navigate("watchlist") },
                    onNavigateToWatched = { navController.navigate("watched") }
                )
            }

            composable("favorites"){
                FavoritesScreen(
                    favoriteMovies = uiState.favoriteMovies,
                    onMovieClick = { movieId ->
                        navController.navigate("movie_detail/$movieId")
                    },
                    onBackClick = { navController.popBackStack() }
                )
            }

            composable("watchlist") {
                WatchlistScreen(
                    movies = uiState.watchlistMovies,
                    onMovieClick = { movieId ->
                        navController.navigate("movie_detail/$movieId")
                    },
                    onBackClick = { navController.popBackStack() }
                )
            }

            composable("watched") {
                WatchedScreen(
                    movies = uiState.watchedMovies,
                    onMovieClick = { movieId ->
                        navController.navigate("movie_detail/$movieId")
                    },
                    onBackClick = { navController.popBackStack() }
                )
            }

            composable("search"){
                val searchUiState = movieListViewModel.searchUiState
                SearchScreen(
                    uiState = searchUiState,
                    onQueryChange = { movieListViewModel.updateSearchQuery(it) },
                    onSearch = { movieListViewModel.performSearch() },
                    onMovieClick = { movieId ->
                        navController.navigate("movie_detail/$movieId")
                    },
                    onBackClick = { navController.popBackStack() }
                )
            }

            composable("movie_detail/{movieId}") { backStackEntry ->
                val movieId = backStackEntry.arguments?.getString("movieId")?.toInt()
                val movie = uiState.movies.find { it.id == movieId }
                    ?: uiState.favoriteMovies.find { it.id == movieId }
                    ?: uiState.watchlistMovies.find { it.id == movieId }
                    ?: uiState.watchedMovies.find { it.id == movieId }
                    ?: movieListViewModel.searchUiState.results.find { it.id == movieId }

                movie?.let {
                    val isFavorite = uiState.favoriteIDs.contains(it.id)

                    MovieDetailScreen(
                        movie = it,
                        isFavorite = isFavorite,
                        onBackClick = { navController.popBackStack() },
                        onToggleFavorite = { movieListViewModel.toggleFavorite(it) }
                    )
                }
            }
        }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieListScreen(
    uiState: MovieListUiState,
    onMovieClick: (Int) -> Unit,
    onRetryClick: () -> Unit,
    onNavigateToFavorites: () -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToWatchlist: () -> Unit,
    onNavigateToWatched: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "CineTrack") },
                actions = {
                    IconButton(onClick = onNavigateToSearch) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Ara"
                        )
                    }
                    IconButton(onClick = onNavigateToWatchlist) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.PlaylistAdd,
                            contentDescription = "İzlemek istediklerim"
                        )
                    }
                    IconButton(onClick = onNavigateToWatched) {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = "İzlediklerim"
                        )
                    }
                    IconButton(onClick = onNavigateToFavorites) {
                        Icon(
                            imageVector = Icons.Filled.Favorite,
                            contentDescription = "Favoriler"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                uiState.errorMessage != null -> {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Bir hata oluştu.")
                        Spacer(modifier = Modifier.padding(top = 4.dp))
                        Text(text = uiState.errorMessage)
                        Spacer(modifier = Modifier.padding(top = 8.dp))
                        Button(onClick = onRetryClick) {
                            Text(text = "Tekrar Dene")
                        }
                    }
                }

                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(uiState.movies.size) { index ->
                            val movie = uiState.movies[index]
                            MoviePosterCard(
                                movie = movie,
                                onClick = { onMovieClick(movie.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

//@Composable
//fun MovieCard(
//    movie: Movie,
//    onClick: () -> Unit
//) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .clickable { onClick() },
//        shape = RoundedCornerShape(12.dp),
//        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
//    ) {
//        Row(
//            modifier = Modifier.padding(12.dp)
//        ) {
//            AsyncImage(
//                model = movie.posterUrl,
//                contentDescription = movie.title,
//                modifier = Modifier
//                    .size(width = 90.dp, height = 130.dp)
//                    .clip(RoundedCornerShape(10.dp)),
//                contentScale = ContentScale.Crop
//            )
//
//            Spacer(modifier = Modifier.padding(start = 12.dp))
//
//            Column(
//                modifier = Modifier
//                    .weight(1f)
//            ) {
//                Text(
//                    text = movie.title,
//                    style = MaterialTheme.typography.titleMedium
//                )
//
//                Spacer(modifier = Modifier.padding(top = 4.dp))
//
//                Text(
//                    text = "${movie.year}",
//                    style = MaterialTheme.typography.bodyMedium
//                )
//
//                Spacer(modifier = Modifier.padding(top = 4.dp))
//
//                Row(
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Icon(
//                        imageVector = Icons.Filled.Star,
//                        contentDescription = "IMDb Rating",
//                        modifier = Modifier.size(18.dp)
//                    )
//                    Spacer(modifier = Modifier.padding(start = 4.dp))
//                    Text(
//                        text = "${movie.rating}",
//                        style = MaterialTheme.typography.bodyMedium
//                    )
//                }
//            }
//        }
//    }
//}

//@Preview(showBackground = true)
//@Composable
//fun MovieListPreview() {
//    val fakeState = MovieListUiState(
//        isLoading = false,
//        movies = sampleMovies
//    )
//
//    MovieListScreen(
//        uiState = fakeState,
//        onMovieClick = {},
//        onRetryClick = {},
//        onNavigateToFavorites = {},
//        onNavigateToSearch = {},
//        onNavigateToWatchlist = {},
//        onNavigateToWatched = {}
//    )
//}
