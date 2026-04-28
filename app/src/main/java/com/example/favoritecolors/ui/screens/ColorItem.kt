package com.example.favoritecolors.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import com.example.favoritecolors.models.ColorToFavorite
import com.example.favoritecolors.ui.viewModel.FavoriteColorsViewModel

@Composable
fun ColorItem(
    color: ColorToFavorite,
    viewModel: FavoriteColorsViewModel,
    showFavoritesCount: Boolean = true
) {
    val contrastColor = Color(color.contrastColor.toColorInt())

    Box(
        modifier = Modifier
            .size(200.dp)
            .background(color = Color(color.color.toColorInt()), shape = RectangleShape)
            .clickable {
                viewModel.handleColorUpdate(color)
            }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = color.displayName, color = contrastColor)
            if (showFavoritesCount) {
                Text(text = color.favoriteCount.toString(), color = contrastColor)
            }
        }
    }
}