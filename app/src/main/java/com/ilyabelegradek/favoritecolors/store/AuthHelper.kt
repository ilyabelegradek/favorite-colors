package com.ilyabelegradek.favoritecolors.store

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

private const val TAG = "AuthHelper.kt"

class AuthHelper @Inject constructor(
    private val auth: FirebaseAuth
) {
    fun getInitialUser(): FirebaseUser? {
        return auth.currentUser
    }

    fun createUser(
        email: String,
        password: String,
        onSignupSuccess: (FirebaseUser?) -> Unit,
        onSignupFailure: (String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    onSignupSuccess(user)
                } else {
                    Log.w(TAG, "firebase error: " + task.exception)
                    onSignupFailure("Error creating a user. Please try again later.")
                }
            }
    }

    fun loginUser(
        email: String,
        password: String,
        onLoginSuccess: (FirebaseUser?) -> Unit,
        onLoginFailure: (String) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    onLoginSuccess(user)
                } else {
                    Log.w(TAG, "firebase error: " + task.exception)
                    onLoginFailure("Error creating a user. Please try again later.")
                }
            }
    }

    fun signOut() {
        auth.signOut()
    }
}