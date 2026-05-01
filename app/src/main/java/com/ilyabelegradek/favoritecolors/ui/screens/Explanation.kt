package com.ilyabelegradek.favoritecolors.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun Explanation() {
    Text(
        text = "Vote for your favorite color! The color with the most votes wins absolutely nothing - but it still feels good to be a winner, right?",
        modifier = Modifier.padding(vertical = 15.dp, horizontal = 15.dp),
        textAlign = TextAlign.Center
    )
}