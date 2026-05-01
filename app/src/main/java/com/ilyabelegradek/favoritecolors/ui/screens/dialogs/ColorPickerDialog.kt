package com.ilyabelegradek.favoritecolors.ui.screens.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import com.ilyabelegradek.favoritecolors.models.DialogState
import com.ilyabelegradek.favoritecolors.ui.state.ColorsState
import com.ilyabelegradek.favoritecolors.ui.viewModel.FavoriteColorsViewModel
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorPickerDialog(viewModel: FavoriteColorsViewModel, state: ColorsState) {
    val controller = rememberColorPickerController()
    val newFavColor = remember { mutableStateOf("") }

    BasicAlertDialog(
        onDismissRequest = { viewModel.setDialogState(DialogState.NONE) },
    ) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier.padding(all = 15.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HsvColorPicker(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(450.dp),
                    controller = controller,
                    onColorChanged = { colorEnvelope: ColorEnvelope ->
                        newFavColor.value = "#${colorEnvelope.hexCode.takeLast(6)}"
                    }
                )

                if (state.user != null) {
                    val currentFavColor = state.user.favoriteColor
                    if (currentFavColor != null) {
                        Text(
                            text = "Current Favorite Color: ${currentFavColor.color}",
                            color = Color(currentFavColor.color.toColorInt())
                        )

                    }
                }
                if (newFavColor.value != "") {
                    Text(
                        text = "New Favorite Color: ${newFavColor.value}",
                        color = Color(newFavColor.value.toColorInt())
                    )
                }

                if (state.colorPickerDialogMessage != "") {
                    Text(
                        text = state.colorPickerDialogMessage,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(vertical = 5.dp),
                        textAlign = TextAlign.Center
                    )
                }

                Button(
                    onClick = { viewModel.saveCustomColor(newFavColor.value) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                ) {
                    Text(text = "SAVE")
                }
            }
        }
    }
}