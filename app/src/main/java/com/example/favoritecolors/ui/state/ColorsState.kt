package com.example.favoritecolors.ui.state

import com.example.favoritecolors.models.ColorToFavorite
import com.example.favoritecolors.models.User

data class ColorsState(
    val colorsToFavorite: List<ColorToFavorite> = listOf<ColorToFavorite>(),
    val showAuthDialog: Boolean = false,
    var signUpTab: Boolean = true,
    var email: String = "",
    var password: String = "",
    var confirmPassword: String = "",
    var authDialogMessage: String = "Sign up to cast your vote!",
    var authDialogError: Boolean = false,
    val passwordRegex: Regex = Regex("^(?=.*[A-Z])(?=.*[@#$%^&+=!]).*$"),
    val user: User? = null,
    val selectedColor: ColorToFavorite? = null,
    val fetchedColorHexCode: String = ""
)