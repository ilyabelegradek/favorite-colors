package com.example.favoritecolors.ui.screens.dialogs

import androidx.compose.runtime.Composable
import com.example.favoritecolors.models.DialogState
import com.example.favoritecolors.ui.state.ColorsState
import com.example.favoritecolors.ui.viewModel.FavoriteColorsViewModel

@Composable
fun DialogWrapper(state: ColorsState, viewModel: FavoriteColorsViewModel) {
    when (state.dialogState) {
        DialogState.NONE -> {}
        DialogState.AUTH -> AuthDialog(viewModel, state)
        DialogState.SORTING -> SortingDialog(viewModel, state)
        DialogState.COLOR_PICKER -> ColorPickerDialog(viewModel, state)
    }
}