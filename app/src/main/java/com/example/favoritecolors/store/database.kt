package com.example.favoritecolors.store

import android.util.Log
import com.example.favoritecolors.models.ColorToFavorite
import com.example.favoritecolors.models.User
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

//fun testWrite(database: DatabaseReference) {
//    try {
//        val colors = listOf(
//            ColorToFavorite(color = "#12a4e3", displayName = "Sky Blue", favoriteCount = 0),
//            ColorToFavorite(color = "#0000FF", displayName = "Blue", favoriteCount = 0),
//            ColorToFavorite(
//                color = "#5af542",
//                displayName = "Neon Green",
//                favoriteCount = 0,
//                contrastColor = "#000000"
//            ),
//            ColorToFavorite(color = "#e3362d", displayName = "Red", favoriteCount = 0),
//            ColorToFavorite(color = "#e3972d", displayName = "Orange", favoriteCount = 0),
//            ColorToFavorite(
//                color = "#e8d827",
//                displayName = "Yellow",
//                favoriteCount = 0,
//                contrastColor = "#000000"
//            ),
//            ColorToFavorite(color = "#6815cf", displayName = "Purple", favoriteCount = 0),
//            ColorToFavorite(color = "#eb17e7", displayName = "Pink", favoriteCount = 0),
//            ColorToFavorite(color = "#000000", displayName = "Black", favoriteCount = 0),
//            ColorToFavorite(
//                color = "#FFFFFF",
//                displayName = "White",
//                favoriteCount = 0,
//                contrastColor = "#e3362d"
//            ),
//        )
//
//        for (color in colors) {
//            writeColor(database, color.color, color.displayName, color.contrastColor)
//        }
//    } catch (e: Exception) {
//        Log.i("ZEBRA", "Error: $e")
//    }
//}
//
//fun writeColor(
//    database: DatabaseReference,
//    hexCode: String,
//    colorName: String,
//    contrastColor: String = ""
//) {
//    val key = database.child("colors").push().key
//    val colorToWrite = ColorToFavorite(
//        uid = key,
//        color = hexCode,
//        displayName = colorName,
//        favoriteCount = 0,
//        contrastColor = contrastColor
//    )
//
//    val colorValues = colorToWrite.toMap()
//    val childUpdates = hashMapOf<String, Any>(
//        "/colors/$key" to colorValues,
//    )
//
//    database.updateChildren(childUpdates)
//}

private const val TAG = "database.kt"

fun getColorsFromDB(database: DatabaseReference, onColorsLoaded: (List<ColorToFavorite>) -> Unit) {
    val colorsQuery = database.child("colors")
    colorsQuery.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val colors = arrayListOf<ColorToFavorite>()
            for (colorSnapshot in dataSnapshot.children) {
                val item: ColorToFavorite? = colorSnapshot.getValue(ColorToFavorite::class.java)
                if (item !== null) {
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
    //val key = database.child("users").push().key
    val newUser = User(
        uid = user.uid,
        email = user.email,
        favoriteColor = selectedColor,
    )

    val userValues = newUser.toMap()
    val childUpdates = hashMapOf<String, Any>(
        "/users/${user.uid}" to userValues,
    )

    database.updateChildren(childUpdates).addOnCompleteListener { task ->
        if (task.isSuccessful) {
            onSuccess()
        } else {
            Log.w(TAG, "Error creating a new user.")
            onFailure()
        }
    }
}

fun getUser(database: DatabaseReference, user: FirebaseUser, onUserLoaded: (User?) -> Unit) {
    database.child("users").child(user.uid).get().addOnSuccessListener { user ->
        val fetchedUser: User? = user.getValue(User::class.java)
        onUserLoaded(fetchedUser)
    }.addOnFailureListener {
        Log.w(TAG, "Error getting user: ", it)
    }
}

//fun getColorsToFavorite(): List<ColorToFavorite> {
//    return listOf(
//        ColorToFavorite("#12a4e3", "Sky Blue", 0, false),
//        ColorToFavorite("#0000FF", "Blue", 0, true),
//        ColorToFavorite("#5af542", "Neon Green", 0, false, contrastColor = "#000000"),
//        ColorToFavorite("#e3362d", "Red", 0, false),
//        ColorToFavorite("#e3972d", "Orange", 0, false),
//        ColorToFavorite("#e8d827", "Yellow", 0, false, contrastColor = "#000000"),
//        ColorToFavorite("#6815cf", "Purple", 0, false),
//        ColorToFavorite("#eb17e7", "Pink", 0, false),
//        ColorToFavorite("#000000", "Black", 0, false),
//        ColorToFavorite("#FFFFFF", "White", 0, false, contrastColor = "#e3362d"),
//    ).shuffled()
//}