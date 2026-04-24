package com.example.favoritecolors.models

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    var uid: String? = "",
    val email: String? = "",
    val favoriteColor: ColorToFavorite? = null,
) {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to uid,
            "email" to email,
            "favoriteColor" to favoriteColor,
        )
    }
}