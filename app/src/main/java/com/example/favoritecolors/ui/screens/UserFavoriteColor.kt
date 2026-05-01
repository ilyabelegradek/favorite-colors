package com.example.favoritecolors.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.favoritecolors.models.DialogState
import com.example.favoritecolors.ui.state.ColorsState
import com.example.favoritecolors.ui.viewModel.FavoriteColorsViewModel

@Composable
fun UserFavoriteColor(state: ColorsState, viewModel: FavoriteColorsViewModel) {
    Column(modifier = Modifier.padding(vertical = 5.dp)) {
        SectionTitle(title = "Your Favorite Color") {
            if (state.user != null) {
                Button(
                    modifier = Modifier.padding(end = 15.dp),
                    onClick = { viewModel.logoutHandler() }) {
                    Text(text = "Sign out")
                }
            } else {
                Button(
                    modifier = Modifier.padding(end = 15.dp),
                    onClick = { viewModel.setDialogState(DialogState.AUTH) }) {
                    Text(text = "Sign in")
                }
            }
        }
        if (state.user != null && state.user.favoriteColor != null) {
            val userFavoriteColor = state.user.favoriteColor
            ColorItem(
                userFavoriteColor,
                viewModel,
                showFavoritesCount = false,
                fullWidth = true
            )
        } else {
            Explanation()
        }
    }
}