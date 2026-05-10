package com.ilyabelegradek.favoritecolors.ui.viewModel

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.ViewModel
import com.ilyabelegradek.favoritecolors.models.ColorToFavorite
import com.ilyabelegradek.favoritecolors.models.DialogState
import com.ilyabelegradek.favoritecolors.models.SortingMethod
import com.ilyabelegradek.favoritecolors.ui.state.ColorsState
import com.google.firebase.database.ValueEventListener
import com.ilyabelegradek.favoritecolors.store.AuthHelper
import com.ilyabelegradek.favoritecolors.store.DatabaseHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

private const val TAG = "FavoriteColorsViewModel"

@HiltViewModel
class FavoriteColorsViewModel @Inject constructor(
    private val databaseHelper: DatabaseHelper,
    private val authHelper: AuthHelper
) : ViewModel() {
    private val _colorsState = MutableStateFlow(ColorsState())
    val colorsState: StateFlow<ColorsState> = _colorsState.asStateFlow()
    private var userListener: ValueEventListener? = null
    private var observedUid: String? = null

    init {
        loadColors()
        initialLogin()
    }

    override fun onCleared() {
        super.onCleared()
        stopObservingUser()
    }

    fun handleColorUpdate(colorToFavorite: ColorToFavorite) {
        if (_colorsState.value.isSubmitting) return

        val user = _colorsState.value.user
        if (user == null) {
            _colorsState.update { it.copy(selectedColor = colorToFavorite) }
            setDialogState(DialogState.AUTH)
        } else if (colorToFavorite.color != user.favoriteColor?.color) {
            _colorsState.update { it.copy(selectedColor = colorToFavorite, isSubmitting = true) }
            databaseHelper.updateUsersFavoriteColor(
                user, colorToFavorite, {},
                {
                    _colorsState.update { it.copy(isSubmitting = false) }
                })
        }
    }

    fun setDialogState(dialogState: DialogState) {
        _colorsState.update { it.copy(dialogState = dialogState) }
    }

    fun setSignupTab(selected: Boolean) {
        val defaultAuthMessage = if (selected) "Sign up to cast your vote!" else ""
        _colorsState.update {
            it.copy(
                signUpTab = selected,
                email = "",
                password = "",
                confirmPassword = "",
                authDialogMessage = defaultAuthMessage,
                authDialogError = false
            )
        }
    }

    fun updateEmail(enteredEmail: String) {
        _colorsState.update { it.copy(email = enteredEmail.trim()) }
    }

    fun updatePassword(enteredPassword: String) {
        _colorsState.update { it.copy(password = enteredPassword.trim()) }
    }

    fun updateConfirmPassword(enteredConfirmPassword: String) {
        _colorsState.update { it.copy(confirmPassword = enteredConfirmPassword.trim()) }
    }

    fun signupHandler() {
        if (validateSignupForm() && !_colorsState.value.isSubmitting) {
            _colorsState.update { it.copy(isSubmitting = true) }
            authHelper.createUser(
                email = _colorsState.value.email,
                password = _colorsState.value.password,
                onSignupSuccess = { firebaseUser ->
                    if (firebaseUser != null) {
                        databaseHelper.writeUser(
                            firebaseUser,
                            _colorsState.value.selectedColor,
                            onSuccess = {
                                startObservingUser(firebaseUser.uid)
                                setDialogState(DialogState.NONE)
                            },
                            onFailure = {
                                _colorsState.update { it.copy(isSubmitting = false) }
                                setDialogState(DialogState.NONE)
                            })
                    } else {
                        Log.w(TAG, "onSignupSuccess but user came back null.")
                        _colorsState.update { it.copy(isSubmitting = false) }
                        setDialogState(DialogState.NONE)
                    }
                },
                onSignupFailure = { errorMessage ->
                    _colorsState.update {
                        it.copy(
                            authDialogError = true,
                            authDialogMessage = errorMessage,
                            isSubmitting = false
                        )
                    }
                })
        }
    }

    fun loginHandler() {
        if (validateLoginForm() && !_colorsState.value.isSubmitting) {
            _colorsState.update { it.copy(isSubmitting = true) }
            authHelper.loginUser(
                email = _colorsState.value.email,
                password = _colorsState.value.password,
                onLoginSuccess = { firebaseUser ->
                    if (firebaseUser != null) {
                        startObservingUser(firebaseUser.uid)
                        setDialogState(DialogState.NONE)
                    } else {
                        _colorsState.update { it.copy(isSubmitting = false) }
                        setDialogState(DialogState.NONE)
                    }
                },
                onLoginFailure = { errorMessage ->
                    _colorsState.update {
                        it.copy(
                            authDialogError = true,
                            authDialogMessage = errorMessage,
                            isSubmitting = false
                        )
                    }
                })
        }
    }

    fun logoutHandler() {
        authHelper.signOut()
        stopObservingUser()
        _colorsState.update { it.copy(user = null) }
    }

    fun setSortingMethod(sortingMethod: SortingMethod) {
        val sortedColors = getSortedColors(_colorsState.value.colorsToFavorite, sortingMethod)

        _colorsState.update {
            it.copy(
                selectedSortingMethod = sortingMethod,
                dialogState = DialogState.NONE,
                colorsToFavorite = sortedColors
            )
        }
    }

    fun saveCustomColor(colorHex: String) {
        for (color in _colorsState.value.colorsToFavorite) {
            if (color.color == colorHex) {
                _colorsState.update { it.copy(colorPickerDialogMessage = "Color already exists! Please vote for it below.") }
                return
            }
        }

        databaseHelper.writeCustomColor(
            user = _colorsState.value.user,
            customColorHex = colorHex,
            onSuccess = {
                _colorsState.update { it.copy(dialogState = DialogState.NONE) }
            },
            onFailure = {})
    }

    private fun getSortedColors(
        favoriteColors: List<ColorToFavorite>,
        sortingMethod: SortingMethod
    ): List<ColorToFavorite> {
        return when (sortingMethod) {
            SortingMethod.COLOR -> favoriteColors.sortedBy { it.color }
            SortingMethod.RANDOM -> favoriteColors.shuffled()
            SortingMethod.MOST_VOTES -> favoriteColors.sortedByDescending { it.favoriteCount }
            SortingMethod.LEAST_VOTES -> favoriteColors.sortedBy { it.favoriteCount }
        }
    }

    private fun loadColors() {
        databaseHelper.getColorsFromDB { favoriteColors ->
            val sortedColors = getSortedColors(
                favoriteColors,
                _colorsState.value.selectedSortingMethod
            )
            _colorsState.update { it.copy(colorsToFavorite = sortedColors) }
        }
    }

    private fun initialLogin() {
        val currentUser = authHelper.getInitialUser()
        if (currentUser != null) {
            startObservingUser(currentUser.uid)
        }
    }

    private fun startObservingUser(uid: String) {
        if (observedUid == uid) return
        stopObservingUser()
        observedUid = uid
        userListener = databaseHelper.observeUser(uid) { fetchedUser ->
            _colorsState.update { it.copy(user = fetchedUser, isSubmitting = false) }
        }
    }

    private fun stopObservingUser() {
        val uid = observedUid
        val listener = userListener
        if (uid != null && listener != null) {
            databaseHelper.stopObservingUser(uid, listener)
        }
        userListener = null
        observedUid = null
    }

    private fun validateSignupForm(): Boolean {
        _colorsState.update { it.copy(authDialogMessage = "", authDialogError = false) }

        if (!isEmailValid()) {
            _colorsState.update {
                it.copy(
                    authDialogMessage = "Invalid email",
                    authDialogError = true
                )
            }
            return false
        } else if (!isPasswordValid()) {
            _colorsState.update {
                it.copy(
                    authDialogMessage = "Password must be at least 8 characters and contain at least one uppercase letter and special character.",
                    authDialogError = true
                )
            }
            return false
        } else if (!isPasswordsMatching()) {
            _colorsState.update {
                it.copy(
                    authDialogMessage = "Passwords do not match.",
                    authDialogError = true
                )
            }
            return false
        }

        return true
    }

    private fun validateLoginForm(): Boolean {
        _colorsState.update { it.copy(authDialogMessage = "", authDialogError = false) }

        if (!isEmailValid()) {
            _colorsState.update {
                it.copy(
                    authDialogMessage = "Invalid email",
                    authDialogError = true
                )
            }
            return false
        } else if (!isPasswordValid()) {
            _colorsState.update {
                it.copy(
                    authDialogMessage = "Password must be at least 8 characters and contain at least one uppercase letter and special character.",
                    authDialogError = true
                )
            }
            return false
        }

        return true
    }

    private fun isEmailValid(): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(_colorsState.value.email).matches()
    }

    private fun isPasswordValid(): Boolean {
        return _colorsState.value.password.length >= 8 && _colorsState.value.password.matches(
            _colorsState.value.passwordRegex
        )
    }

    private fun isPasswordsMatching(): Boolean {
        return _colorsState.value.password == _colorsState.value.confirmPassword
    }
}