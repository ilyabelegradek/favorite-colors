package com.example.favoritecolors.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.favoritecolors.ui.state.ColorsState
import com.example.favoritecolors.ui.viewModel.FavoriteColorsViewModel

@Composable
fun UserFavoriteColor(state: ColorsState, viewModel: FavoriteColorsViewModel) {
    if (state.user != null) {
        val userFavoriteColor = state.user.favoriteColor
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
    } else {
        Explanation()
    }
}