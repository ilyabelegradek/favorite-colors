package com.example.favoritecolors.ui.state

import com.example.favoritecolors.models.ColorToFavorite
import com.example.favoritecolors.models.DialogState
import com.example.favoritecolors.models.SortingMethod
import com.example.favoritecolors.models.User

data class ColorsState(
    val colorsToFavorite: List<ColorToFavorite> = listOf<ColorToFavorite>(),
    val dialogState: DialogState = DialogState.NONE,
    var signUpTab: Boolean = true,
    var email: String = "",
    var password: String = "",
    var confirmPassword: String = "",
    var authDialogMessage: String = "Sign up to cast your vote!",
    var authDialogError: Boolean = false,
    val passwordRegex: Regex = Regex("^(?=.*[A-Z])(?=.*[@#$%^&+=!]).*$"),
    val user: User? = null,
    val selectedColor: ColorToFavorite? = null,
    val fetchedColorHexCode: String = "",
    val isSubmitting: Boolean = false,
    val selectedSortingMethod: SortingMethod = SortingMethod.COLOR,
    val colorPickerDialogMessage: String = ""
)