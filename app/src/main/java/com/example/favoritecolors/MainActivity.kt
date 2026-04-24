package com.example.favoritecolors

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.favoritecolors.ui.screens.ColorGrid
import com.example.favoritecolors.ui.screens.Explanation
import com.example.favoritecolors.ui.theme.FavoriteColorsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.GRAY),
            navigationBarStyle = SystemBarStyle.dark(
                Color.GRAY
            )
        )
        setContent {
            FavoriteColorsTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .navigationBarsPadding()
                        .statusBarsPadding()
                ) {
                    Column() {
                        Explanation()
                        ColorGrid()
                    }
                }
            }
        }
    }
}