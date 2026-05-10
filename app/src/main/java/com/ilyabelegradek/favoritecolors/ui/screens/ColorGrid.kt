package com.ilyabelegradek.favoritecolors.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ilyabelegradek.favoritecolors.models.DialogState
import com.ilyabelegradek.favoritecolors.ui.state.ColorsState
import com.ilyabelegradek.favoritecolors.ui.viewModel.FavoriteColorsViewModel

@Composable
fun ColorGrid(state: ColorsState, viewModel: FavoriteColorsViewModel = viewModel()) {
    SectionTitle(title = "Pick A Favorite Color") {
        Button(
            modifier = Modifier.padding(end = 15.dp),
            onClick = { viewModel.setDialogState(DialogState.SORTING) }) {
            Text(text = "Sort Colors")
        }
    }
    LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 200.dp)) {
        items(items = state.colorsToFavorite, key = { item -> item.color }) { color ->
            ColorItem(
                color = color,
                viewModel = viewModel,
                isSubmitting = state.isSubmitting,
                modifier = Modifier.animateItem()
            )
        }
    }
}

