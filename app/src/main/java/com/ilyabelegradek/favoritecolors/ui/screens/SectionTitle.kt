package com.ilyabelegradek.favoritecolors.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SectionTitle(
    title: String,
    content: (@Composable () -> Unit)? = null
) {
    val arrangement = if (content != null) Arrangement.SpaceBetween else Arrangement.Center

    HorizontalDivider(thickness = 1.dp, color = Color.Gray)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        horizontalArrangement = arrangement,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, modifier = Modifier.padding(start = 15.dp))
        if (content != null)
            content()
    }
    HorizontalDivider(thickness = 1.dp, color = Color.Gray)
}