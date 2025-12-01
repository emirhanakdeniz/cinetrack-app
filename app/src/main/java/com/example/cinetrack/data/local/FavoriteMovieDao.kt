package com.example.cinetrack.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteMovieDao {
    @Query("SELECT * FROM favorite_movies")
    suspend fun getAllFavorites(): List<FavoriteMovieEntity>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_movies WHERE id = :movieId)")
    suspend fun isFavorite(movieId: Int): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(movie: FavoriteMovieEntity)

    @Delete
    suspend fun deleteFavorite(movie: FavoriteMovieEntity)
}