package com.example.favoritecolors.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import com.example.favoritecolors.models.ColorToFavorite
import com.example.favoritecolors.models.DialogState
import com.example.favoritecolors.ui.viewModel.FavoriteColorsViewModel

@Composable
fun UserColorItem(
    color: ColorToFavorite?,
    viewModel: FavoriteColorsViewModel,
) {
    if (color == null) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(
                    color = Color.LightGray.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(size = 12.dp)
                )
                .clickable {
                    viewModel.setDialogState(DialogState.COLOR_PICKER)
                }
                .border(
                    width = 2.dp,
                    color = Color.Gray,
                    shape = RoundedCornerShape(12.dp) // Use a dashed path for the 'Idle' state
                )
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Choose a Custom Color")
            }
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(
                    color = Color(color.color.toColorInt()),
                    shape = RoundedCornerShape(size = 12.dp)
                )
                .clickable {
                    viewModel.setDialogState(DialogState.COLOR_PICKER)
                }
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = color.color,
                    color = Color(color.contrastColor.toColorInt())
                )
            }
        }
    }
}