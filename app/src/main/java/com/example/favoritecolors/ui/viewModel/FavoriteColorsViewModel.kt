package com.example.favoritecolors.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.favoritecolors.store.createUser
import com.example.favoritecolors.store.getColorsFromDB
import com.example.favoritecolors.store.loginUser
import com.example.favoritecolors.models.ColorToFavorite
import com.example.favoritecolors.store.getUser
import com.example.favoritecolors.store.getUserById
import com.example.favoritecolors.store.updateUsersFavoriteColor
import com.example.favoritecolors.store.writeUser
import com.example.favoritecolors.ui.state.ColorsState
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val TAG = "FavoriteColorsViewModel"

class FavoriteColorsViewModel : ViewModel() {
    private val _colorsState = MutableStateFlow(ColorsState())
    val colorsState: StateFlow<ColorsState> = _colorsState.asStateFlow()
    private var database: DatabaseReference = Firebase.database.reference
    private var auth: FirebaseAuth = Firebase.auth

    init {
        loadColors()
        initialLogin()
    }

    fun handleColorUpdate(colorToFavorite: ColorToFavorite) {
        _colorsState.value =
            _colorsState.value.copy(selectedColor = colorToFavorite)
        val user = _colorsState.value.user
        if (user == null) {
            toggleRegistrationDialog()
        } else if (colorToFavorite != user.favoriteColor) {
            viewModelScope.launch {
                updateUsersFavoriteColor(database, user, colorToFavorite, {
                    val uid = user.uid
                    if (uid != null) {
                        getUserById(database, uid, onUserLoaded = { fetchedUser ->
                            _colorsState.value = _colorsState.value.copy(
                                user = fetchedUser,
                                isSubmitting = false
                            )
                        })
                    }
                }, {})
            }
        }
    }

    fun toggleRegistrationDialog() {
        _colorsState.value =
            _colorsState.value.copy(showAuthDialog = !_colorsState.value.showAuthDialog)
    }

    fun setSignupTab(selected: Boolean) {
        val defaultAuthMessage = if (selected) "Sign up to cast your vote!" else ""
        _colorsState.value =
            _colorsState.value.copy(
                signUpTab = selected,
                email = "",
                password = "",
                confirmPassword = "",
                authDialogMessage = defaultAuthMessage,
                authDialogError = false
            )
    }

    fun updateEmail(enteredEmail: String) {
        _colorsState.value =
            _colorsState.value.copy(email = enteredEmail.trim())
    }

    fun updatePassword(enteredPassword: String) {
        _colorsState.value =
            _colorsState.value.copy(password = enteredPassword.trim())
    }

    fun updateConfirmPassword(enteredConfirmPassword: String) {
        _colorsState.value =
            _colorsState.value.copy(confirmPassword = enteredConfirmPassword.trim())
    }

    fun signupHandler() {
        if (validateSignupForm() && !_colorsState.value.isSubmitting) {
            _colorsState.value = _colorsState.value.copy(isSubmitting = true)
            createUser(
                auth,
                email = _colorsState.value.email,
                password = _colorsState.value.password,
                onSignupSuccess = { firebaseUser ->
                    if (firebaseUser != null) {
                        writeUser(
                            database,
                            firebaseUser,
                            _colorsState.value.selectedColor,
                            onSuccess = {
                                getUser(database, firebaseUser, onUserLoaded = { fetchedUser ->
                                    _colorsState.value = _colorsState.value.copy(
                                        user = fetchedUser,
                                        isSubmitting = false
                                    )
                                    toggleRegistrationDialog()
                                })
                            },
                            onFailure = {
                                _colorsState.value = _colorsState.value.copy(isSubmitting = false)
                                toggleRegistrationDialog()
                            })
                    } else {
                        Log.w(TAG, "onSignupSuccess but user came back null.")
                        _colorsState.value = _colorsState.value.copy(isSubmitting = false)
                        toggleRegistrationDialog()
                    }
                },
                onSignupFailure = { errorMessage ->
                    _colorsState.value = _colorsState.value.copy(
                        authDialogError = true,
                        authDialogMessage = errorMessage,
                        isSubmitting = false
                    )
                })
        }
    }

    fun loginHandler() {
        if (validateLoginForm() && !_colorsState.value.isSubmitting) {
            _colorsState.value = _colorsState.value.copy(isSubmitting = true)
            loginUser(
                auth,
                email = _colorsState.value.email,
                password = _colorsState.value.password,
                onLoginSuccess = { firebaseUser ->
                    if (firebaseUser != null) {
                        getUser(database, firebaseUser, onUserLoaded = { fetchedUser ->
                            _colorsState.value = _colorsState.value.copy(
                                user = fetchedUser,
                                isSubmitting = false
                            )
                            toggleRegistrationDialog()
                        })
                    } else {
                        _colorsState.value = _colorsState.value.copy(isSubmitting = false)
                        toggleRegistrationDialog()
                    }
                },
                onLoginFailure = { errorMessage ->
                    _colorsState.value = _colorsState.value.copy(
                        authDialogError = true,
                        authDialogMessage = errorMessage,
                        isSubmitting = false
                    )
                })
        }
    }

    fun logoutHandler() {
        auth.signOut()
        _colorsState.value = _colorsState.value.copy(user = null)
    }

    private fun loadColors() {
        getColorsFromDB(database) { favoriteColors ->
            _colorsState.value = _colorsState.value.copy(colorsToFavorite = favoriteColors)
        }
    }

    private fun initialLogin() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            getUser(database, currentUser, onUserLoaded = { fetchedUser ->
                _colorsState.value = _colorsState.value.copy(user = fetchedUser)
            })
        }
    }

    private fun validateSignupForm(): Boolean {
        _colorsState.value = _colorsState.value.copy(
            authDialogMessage = "",
            authDialogError = false
        )

        if (!isEmailValid()) {
            _colorsState.value =
                _colorsState.value.copy(authDialogMessage = "Invalid email", authDialogError = true)
            return false
        } else if (!isPasswordValid()) {
            _colorsState.value = _colorsState.value.copy(
                authDialogMessage = "Password must be at least 8 characters and contain at least one uppercase letter and special character.",
                authDialogError = true
            )
            return false
        } else if (!isPasswordsMatching()) {
            _colorsState.value = _colorsState.value.copy(
                authDialogMessage = "Passwords do not match.",
                authDialogError = true
            )
            return false
        }

        return true
    }

    private fun validateLoginForm(): Boolean {
        _colorsState.value = _colorsState.value.copy(
            authDialogMessage = "",
            authDialogError = false
        )

        if (!isEmailValid()) {
            _colorsState.value =
                _colorsState.value.copy(authDialogMessage = "Invalid email", authDialogError = true)
            return false
        } else if (!isPasswordValid()) {
            _colorsState.value = _colorsState.value.copy(
                authDialogMessage = "Password must be at least 8 characters and contain at least one uppercase letter and special character.",
                authDialogError = true
            )
            return false
        }

        return true
    }

    private fun isEmailValid(): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(_colorsState.value.email).matches()
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