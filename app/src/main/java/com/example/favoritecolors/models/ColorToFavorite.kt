package com.example.favoritecolors.models

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class ColorToFavorite(
    var uid: String? = "",
    val color: String = "",
    val displayName: String = "",
    val favoriteCount: Int = 0,
    val contrastColor: String = "#FFFFFF"
) {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to uid,
            "color" to color,
            "displayName" to displayName,
            "favoriteCount" to favoriteCount,
            "contrastColor" to contrastColor,
        )
    }

    override fun toString(): String {
        return this.displayName + ": " + this.color
    }
}