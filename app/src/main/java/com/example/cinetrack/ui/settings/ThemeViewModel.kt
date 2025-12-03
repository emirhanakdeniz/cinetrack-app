package com.example.cinetrack.ui.settings

import android.app.Application
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinetrack.data.preferences.ThemeKeys
import com.example.cinetrack.data.preferences.themeDataStore
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ThemeViewModel(app: Application) : AndroidViewModel(app) {
    val themeMode = app.themeDataStore.data.map { prefs ->
        prefs[ThemeKeys.THEME_MODE] ?: 0 // 0 = system
    }.stateIn(viewModelScope, SharingStarted.Eagerly, 0)

    fun setTheme(mode: Int) {
        viewModelScope.launch {
            getApplication<Application>().themeDataStore.edit { prefs ->
                prefs[ThemeKeys.THEME_MODE] = mode
            }
        }
    }
}