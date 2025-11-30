package com.example.cinetrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CineTrackApp()
        }
    }
}

@Composable
fun CineTrackApp(){
    MaterialTheme {
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = "movie_list"
        ) {
            composable("movie_list") {
                MovieListScreen(
                    movies = sampleMovies,
                    onMovieClick = { movieId ->
                        navController.navigate("movie_detail/$movieId")
                    }
                )
            }

            composable("movie_detail/{movieId}") { backStackEntry ->
                val movieId = backStackEntry.arguments?.getString("movieId")?.toIntOrNull()
                val movie = sampleMovies.find { it.id == movieId }

                movie?.let {
                    MovieDetailScreen(it)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieListScreen(
    movies: List<Movie>,
    onMovieClick: (Int) -> Unit
){
    Scaffold(
        topBar = {
            TopAppBar(
                title = {Text(text = "CineTrack")}
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)

        ) {
            items(movies) { movie ->
                MovieCard(movie = movie, onclick = { onMovieClick(movie.id) })
            }
        }
    }
}

@Composable
fun MovieCard(movie: Movie , onclick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable{ onclick()},
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp)
        ) {
            AsyncImage(
                model = movie.posterUrl,
                contentDescription = movie.title,
                modifier = Modifier
                    .size(width = 90.dp, height = 130.dp)
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.padding(start = 12.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer( modifier = Modifier.padding(top = 4.dp))
                Text(
                    text = "${movie.year}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer( modifier = Modifier.padding(top = 4.dp))
                Text(
                    text = "IMDb: ${movie.rating}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

//@Composable
//fun MovieDetailScreenPreview(movie: Movie) {
//    // Placeholder for the detail screen
//    Text("Details for ${movie.title}")
//}

@Preview(showBackground = true)
@Composable
fun MovieListPreview() {
    MaterialTheme {
        MovieListScreen(movies = sampleMovies, onMovieClick = {})
    }
}
