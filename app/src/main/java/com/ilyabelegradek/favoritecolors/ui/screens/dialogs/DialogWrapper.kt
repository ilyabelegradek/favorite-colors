package com.ilyabelegradek.favoritecolors.ui.screens.dialogs

import androidx.compose.runtime.Composable
import com.ilyabelegradek.favoritecolors.models.DialogState
import com.ilyabelegradek.favoritecolors.ui.state.ColorsState
import com.ilyabelegradek.favoritecolors.ui.viewModel.FavoriteColorsViewModel

@Composable
fun DialogWrapper(state: ColorsState, viewModel: FavoriteColorsViewModel) {
    when (state.dialogState) {
        DialogState.NONE -> {}
        DialogState.AUTH -> AuthDialog(viewModel, state)
        DialogState.SORTING -> SortingDialog(viewModel, state)
        DialogState.COLOR_PICKER -> ColorPickerDialog(viewModel, state)
    }
}