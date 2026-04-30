package com.example.favoritecolors.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.example.favoritecolors.ui.state.ColorsState
import com.example.favoritecolors.ui.viewModel.FavoriteColorsViewModel
import com.example.favoritecolors.models.SORTING_LEAST_VOTES
import com.example.favoritecolors.models.SORTING_MOST_VOTES
import com.example.favoritecolors.models.SORTING_RANDOM


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortingDialog(viewModel: FavoriteColorsViewModel, state: ColorsState) {
    BasicAlertDialog(
        onDismissRequest = { viewModel.toggleSortingDialog() },
    ) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 6.dp
        ) {
            Column(modifier = Modifier.selectableGroup()) {
                state.sortingMethods.forEach { text ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .selectable(
                                selected = (text == state.selectedSortingMethod),
                                onClick = { viewModel.setSortingMethod(text) },
                                role = Role.RadioButton
                            )
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (text == state.selectedSortingMethod),
                            onClick = null
                        )
                        Text(
                            text = getRadioButtonTranslation(text),
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            }
        }
    }
}

fun getRadioButtonTranslation(radioText: String): String {
    return when (radioText) {
        SORTING_RANDOM -> "Random"
        SORTING_MOST_VOTES -> "Most Votes"
        SORTING_LEAST_VOTES -> "Least Votes"
        else -> ""
    }
}