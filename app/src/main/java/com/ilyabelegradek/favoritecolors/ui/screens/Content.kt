package com.ilyabelegradek.favoritecolors.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.ilyabelegradek.favoritecolors.ui.screens.dialogs.DialogWrapper
import com.ilyabelegradek.favoritecolors.ui.viewModel.FavoriteColorsViewModel

@Composable
fun Content(viewModel: FavoriteColorsViewModel = hiltViewModel()) {
    val state by viewModel.colorsState.collectAsState()

    UserFavoriteColor(state, viewModel)
    ColorGrid(state, viewModel)
    DialogWrapper(state, viewModel)
}