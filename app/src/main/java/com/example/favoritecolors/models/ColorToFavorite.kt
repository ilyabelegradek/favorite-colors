package com.example.favoritecolors.models

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class ColorToFavorite(
    var uid: String? = "",
    val color: String = "",
    val favoriteCount: Int = 0,
    var contrastColor: String = "",
) {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to uid,
            "color" to color,
            "favoriteCount" to favoriteCount,
            "contrastColor" to contrastColor,
        )
    }
}