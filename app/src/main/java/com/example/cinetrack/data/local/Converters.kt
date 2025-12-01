package com.example.cinetrack.data.local

import androidx.room.TypeConverter
import com.example.cinetrack.MovieStatus

class Converters {
    @TypeConverter
    fun fromStatus(status: MovieStatus?) : String? {
        return status?.name
    }

    @TypeConverter
    fun toStatus(value: String?): MovieStatus? {
        return value?.let { MovieStatus.valueOf(it) }
    }
}