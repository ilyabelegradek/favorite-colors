package com.example.favoritecolors.store

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

private const val TAG = "auth.kt"

fun createUser(
    auth: FirebaseAuth,
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
    auth: FirebaseAuth,
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