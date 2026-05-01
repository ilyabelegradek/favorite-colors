package com.example.favoritecolors.store

import android.util.Log
import com.example.favoritecolors.models.ColorToFavorite
import com.example.favoritecolors.models.User
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ValueEventListener

private const val TAG = "database.kt"

fun getColorsFromDB(database: DatabaseReference, onColorsLoaded: (List<ColorToFavorite>) -> Unit) {
    val colorsQuery = database.child("colors")
    colorsQuery.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val colors = arrayListOf<ColorToFavorite>()
            for (colorSnapshot in dataSnapshot.children) {
                val item: ColorToFavorite? = colorSnapshot.getValue(ColorToFavorite::class.java)
                if (item !== null) {
                    item.uid = colorSnapshot.key
                    colors.add(item)
                }
            }
            onColorsLoaded(colors)
        }

        override fun onCancelled(databaseError: DatabaseError) {
            Log.w(TAG, "loadColorsFailed: ", databaseError.toException())
        }
    })
}

fun writeUser(
    database: DatabaseReference,
    user: FirebaseUser,
    selectedColor: ColorToFavorite? = null,
    onSuccess: () -> Unit,
    onFailure: () -> Unit
) {
    val newUser = User(
        uid = user.uid,
        favoriteColor = selectedColor,
    )

    val userValues = newUser.toMap()
    val childUpdates = hashMapOf<String, Any>(
        "/users/${user.uid}" to userValues,
    )

    val colorUid = selectedColor?.uid
    if (!colorUid.isNullOrEmpty()) {
        childUpdates["/colors/$colorUid/favoriteCount"] = ServerValue.increment(1.0)
    }

    database.updateChildren(childUpdates).addOnCompleteListener { task ->
        if (task.isSuccessful) {
            onSuccess()
        } else {
            Log.w(TAG, "Error creating a new user. " + task.exception)
            onFailure()
        }
    }
}

fun updateUsersFavoriteColor(
    database: DatabaseReference,
    user: User,
    selectedColor: ColorToFavorite,
    onSuccess: () -> Unit,
    onFailure: () -> Unit
) {
    val colorValues = selectedColor.toMap()
    val childUpdates = hashMapOf<String, Any>("/users/${user.uid}/favoriteColor" to colorValues)
    val oldFavoriteUid = user.favoriteColor?.uid
    if (!oldFavoriteUid.isNullOrEmpty()) {
        childUpdates["/colors/$oldFavoriteUid/favoriteCount"] = ServerValue.increment(-1.0)
    }
    val newFavoriteUid = selectedColor.uid
    childUpdates["/colors/$newFavoriteUid/favoriteCount"] = ServerValue.increment(1.0)

    database.updateChildren(childUpdates).addOnCompleteListener { task ->
        if (task.isSuccessful) {
            onSuccess()
        } else {
            Log.w(TAG, "Error updating favorite color: " + task.exception)
            onFailure()
        }
    }
}

fun writeCustomColor(
    database: DatabaseReference,
    user: User?,
    customColorHex: String,
    onSuccess: () -> Unit,
    onFailure: () -> Unit
) {
    if (user == null) {
        onFailure()
        return
    }

    val key = database.child("colors").push().key
    if (key == null) {
        onFailure()
        return
    }

    val customColor = ColorToFavorite(
        uid = key,
        color = customColorHex,
        createdByUser = user.uid,
        favoriteCount = 1
    )
    val colorValues = customColor.toMap()
    val childUpdates = hashMapOf<String, Any>(
        "/colors/${key}" to colorValues,
        "/users/${user.uid}/favoriteColor" to colorValues
    )

    database.updateChildren(childUpdates).addOnCompleteListener { task ->
        if (task.isSuccessful) {
            onSuccess()
        } else {
            Log.w(TAG, "Error creating custom color: " + task.exception)
            onFailure()
        }
    }
}

fun observeUser(
    database: DatabaseReference,
    uid: String,
    onUserLoaded: (User?) -> Unit
): ValueEventListener {
    val userListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val fetchedUser = snapshot.getValue(User::class.java)
            onUserLoaded(fetchedUser)
        }

        override fun onCancelled(error: DatabaseError) {
            Log.w(TAG, "observeUser:onCancelled", error.toException())
        }
    }
    database.child("users").child(uid).addValueEventListener(userListener)
    return userListener
}