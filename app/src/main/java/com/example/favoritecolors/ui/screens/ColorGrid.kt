package com.example.favoritecolors.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.favoritecolors.ui.viewModel.FavoriteColorsViewModel

@Composable
fun ColorGrid(viewModel: FavoriteColorsViewModel = viewModel()) {
    val colorUiState by viewModel.colorsState.collectAsState()

    if (colorUiState.user != null) {
        val userFavoriteColor = colorUiState.user?.favoriteColor
        Column(modifier = Modifier.padding(vertical = 5.dp)) {
            if (userFavoriteColor != null) {
                ColorItem(
                    userFavoriteColor,
                    viewModel
                )
            }
            Button(onClick = { viewModel.logoutHandler() }) {
                Text(text = "Sign out")
            }
        }
    }

    LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 200.dp)) {
        items(colorUiState.colorsToFavorite) { color ->
            ColorItem(
                color,
                viewModel
            )
        }
    }

    if (colorUiState.showAuthDialog) {
        AuthDialog(viewModel, colorUiState)
    }
}

