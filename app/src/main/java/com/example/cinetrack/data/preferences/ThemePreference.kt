package com.example.cinetrack.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

val Context.themeDataStore by preferencesDataStore("theme_prefs")

object ThemeKeys {
    val THEME_MODE = intPreferencesKey("theme_mode")
}

// 0 = System default
// 1 = Light
// 2 = Dark