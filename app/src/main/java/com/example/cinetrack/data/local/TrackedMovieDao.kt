package com.example.cinetrack.data.local

import androidx.room.*
import com.example.cinetrack.MovieStatus

@Dao
interface TrackedMovieDao {

    @Query("SELECT * FROM tracked_movies")
    suspend fun getAllTracked(): List<TrackedMovieEntity>

    @Query("SELECT * FROM tracked_movies WHERE isFavorite = 1")
    suspend fun getFavorites(): List<TrackedMovieEntity>

    @Query("SELECT * FROM tracked_movies WHERE status = :status")
    suspend fun getByStatus(status: MovieStatus): List<TrackedMovieEntity>

    @Query("SELECT * FROM tracked_movies WHERE id = :movieId LIMIT 1")
    suspend fun getById(movieId: Int): TrackedMovieEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(movie: TrackedMovieEntity)

    @Query("UPDATE tracked_movies SET isFavorite = :isFavorite WHERE id = :movieId")
    suspend fun updateFavorite(movieId: Int, isFavorite: Boolean)

    @Query("UPDATE tracked_movies SET status = :status WHERE id = :movieId")
    suspend fun updateStatus(movieId: Int, status: MovieStatus)
}
