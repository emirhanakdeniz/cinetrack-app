package com.example.cinetrack.ui.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.cinetrack.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    themeMode: Int, onThemeChange: (Int) -> Unit, onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Ayarlar") }, navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Geri")
                }
            })
        }) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp)
        ) {
            Text(
                "Tema", style = MaterialTheme.typography.titleMedium
            )

            Spacer(Modifier.height(8.dp))

            val options = listOf(
                "Sistem Varsayılanı", // 0
                "Açık Mod", // 1
                "Karanlık Mod" // 2
            )

            options.forEachIndexed { index, label ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                ) {
                    RadioButton(
                        selected = themeMode == index, onClick = { onThemeChange(index) })
                    Spacer(Modifier.width(8.dp))
                    Text(label)
                }
            }

//            Spacer(Modifier.height(32.dp))
//
//            Divider()
//
//            Spacer(Modifier.height(16.dp))
//
//            // APP INFO
//            Text(
//                "Uygulama Bilgisi", style = MaterialTheme.typography.titleMedium
//            )

            Spacer(Modifier.height(160.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.cinetrack_logo_splash),
                    contentDescription = "Logo",
                    modifier = Modifier.size(100.dp)
                )

                Spacer(Modifier.height(12.dp))

                Text("CineTrack v0.4.7beta")
                Text(
                    "Sevgiyle <3", style = MaterialTheme.typography.bodySmall
                )
            }

        }
    }
}

