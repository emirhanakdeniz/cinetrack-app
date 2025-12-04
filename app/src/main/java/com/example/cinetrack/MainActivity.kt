package com.example.cinetrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cinetrack.ui.components.HorizontalMovieRow
import com.example.cinetrack.ui.components.SectionTitle
import com.example.cinetrack.ui.home.RecommendationPlaceholder
import com.example.cinetrack.ui.settings.SettingsScreen
import com.example.cinetrack.ui.settings.ThemeViewModel
import com.example.cinetrack.ui.theme.CineGold
import com.example.cinetrack.ui.theme.CineTrackTheme

class MainActivity : ComponentActivity() {
    private val themeViewModel: ThemeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeMode by themeViewModel.themeMode.collectAsState()
            CineTrackTheme(themeMode = themeMode) {
                CineTrackApp(
                    themeMode = themeMode, onThemeChange = { themeViewModel.setTheme(it) })
            }
        }
    }
}

@Composable
fun CineTrackApp(
    themeMode: Int, onThemeChange: (Int) -> Unit
) {
    val navController = rememberNavController()
    val movieListViewModel: MovieListViewModel = viewModel()

    val uiState = movieListViewModel.uiState

    NavHost(
        navController = navController, startDestination = "splash"
    ) {
        composable("splash") {
            SplashScreen(
                onSplashFinished = {
                    navController.navigate("movie_list") {
                        popUpTo("splash") { inclusive = true }
                    }
                })
        }
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
                onNavigateToWatched = { navController.navigate("watched") },
                onNavigateToSettings = { navController.navigate("settings") })
        }

        composable("favorites") {
            FavoritesScreen(favoriteMovies = uiState.favoriteMovies, onMovieClick = { movieId ->
                navController.navigate("movie_detail/$movieId")
            }, onBackClick = { navController.popBackStack() })
        }

        composable("watchlist") {
            WatchlistScreen(movies = uiState.watchlistMovies, onMovieClick = { movieId ->
                navController.navigate("movie_detail/$movieId")
            }, onBackClick = { navController.popBackStack() })
        }

        composable("watched") {
            WatchedScreen(movies = uiState.watchedMovies, onMovieClick = { movieId ->
                navController.navigate("movie_detail/$movieId")
            }, onBackClick = { navController.popBackStack() })
        }

        composable("search") {
            val searchUiState = movieListViewModel.searchUiState
            SearchScreen(
                uiState = searchUiState,
                onQueryChange = { movieListViewModel.updateSearchQuery(it) },
                onSearch = { movieListViewModel.performSearch() },
                onMovieClick = { movieId ->
                    navController.navigate("movie_detail/$movieId")
                },
                onBackClick = { navController.popBackStack() })
        }

        composable("settings") {
            SettingsScreen(
                themeMode = themeMode,
                onThemeChange = onThemeChange,
                onBack = { navController.popBackStack() })
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

                val currentStatus = when {
                    uiState.watchlistMovies.any { m -> m.id == it.id } -> MovieStatus.WATCHLIST
                    uiState.watchedMovies.any { m -> m.id == it.id } -> MovieStatus.WATCHED
                    else -> MovieStatus.NONE
                }

                MovieDetailScreen(
                    movie = it,
                    isFavorite = isFavorite,
                    status = currentStatus,
                    onBackClick = { navController.popBackStack() },
                    onToggleFavorite = { movieListViewModel.toggleFavorite(it) },
                    onSetStatus = { status -> movieListViewModel.setMovieStatus(it, status) })
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
    onNavigateToWatched: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { CineTrackAppBarTitle() }, actions = {
                IconButton(onClick = onNavigateToSearch) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Ara",
                    )
                }
//                IconButton(onClick = onNavigateToWatchlist) {
//                    Icon(
//                        imageVector = Icons.AutoMirrored.Filled.PlaylistAdd,
//                        contentDescription = "İzlemek istediklerim",
//                        tint = CineGold
//                    )
//                }
//                IconButton(onClick = onNavigateToWatched) {
//                    Icon(
//                        imageVector = Icons.Filled.CheckCircle,
//                        contentDescription = "İzlediklerim",
//                        tint = CineGold
//                    )
//                }
                IconButton(onClick = onNavigateToFavorites) {
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = "Favoriler",
                        tint = CineGold

                    )
                }
                IconButton(onClick = onNavigateToSettings) {
                    Icon(
                        imageVector = Icons.Filled.Settings, contentDescription = "Ayarlar"
                    )
                }
            })
        }) { innerPadding ->
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
                        Text(text = "Filmler yüklenemedi 😕")
                        Spacer(modifier = Modifier.padding(top = 4.dp))
                        Text(
                            text = "İnternet bağlantını veya TMDb servisini kontrol et.\nİstersen tekrar deneyebilirsin.",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Spacer(modifier = Modifier.padding(top = 8.dp))
                        Button(onClick = onRetryClick) {
                            Text(text = "Tekrar dene")
                        }

                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // FOR YOU PAGE
                        item {
                            SectionTitle(title = "Senin İçin Önerilenler")
                            Spacer(modifier = Modifier.height(8.dp))
                            if (uiState.recommendedMovies.isNotEmpty()) {
                                HorizontalMovieRow(
                                    movies = uiState.recommendedMovies, onMovieClick = onMovieClick
                                )
                            } else {
                                RecommendationPlaceholder(
                                    hasFavorites = uiState.favoriteMovies.isNotEmpty() || uiState.watchedMovies.isNotEmpty() || uiState.watchlistMovies.isNotEmpty()
                                )
                            }
                        }

                        // POPULAR MOVEIS
                        if (uiState.movies.isNotEmpty()) {
                            item {
                                SectionTitle(title = "Popüler Filmler")
                                Spacer(modifier = Modifier.height(8.dp))
                                HorizontalMovieRow(
                                    movies = uiState.movies, onMovieClick = onMovieClick
                                )
                            }
                        }

                        // WATCH LIST
                        if (uiState.watchlistMovies.isNotEmpty()) {
                            item {
                                SectionTitle(title = "İzleme listen")
                                Spacer(modifier = Modifier.height(8.dp))
                                HorizontalMovieRow(
                                    movies = uiState.watchlistMovies, onMovieClick = onMovieClick
                                )
                            }
                        }

                        // WATCHED LIST
                        if (uiState.watchedMovies.isNotEmpty()) {
                            item {
                                SectionTitle(title = "İzlediklerin")
                                Spacer(modifier = Modifier.height(8.dp))
                                HorizontalMovieRow(
                                    movies = uiState.watchedMovies, onMovieClick = onMovieClick
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CineTrackAppBarTitle() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(id = R.drawable.ic_cinetrack_logo),
            contentDescription = "CineTrack logo",
            modifier = Modifier.width(28.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "CineTrack")
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
