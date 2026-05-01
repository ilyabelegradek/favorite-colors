package com.example.favoritecolors.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.favoritecolors.ui.screens.dialogs.DialogWrapper
import com.example.favoritecolors.ui.viewModel.FavoriteColorsViewModel

@Composable
fun Content(viewModel: FavoriteColorsViewModel = viewModel()) {
    val state by viewModel.colorsState.collectAsState()

    UserFavoriteColor(state, viewModel)
    ColorGrid(state, viewModel)
    DialogWrapper(state, viewModel)
}