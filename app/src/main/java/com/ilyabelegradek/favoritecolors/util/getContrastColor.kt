package com.ilyabelegradek.favoritecolors.util

fun getContrastColor(hexColor: String): String {
    val color = hexColor.removePrefix("#").toInt(16)

    val r = (color shr 16) and 0xFF
    val g = (color shr 8) and 0xFF
    val b = color and 0xFF

    // YIQ formula to determine perceived brightness
    val yiq = ((r * 299) + (g * 587) + (b * 114)) / 1000

    return if (yiq >= 128) "#000000" else "#FFFFFF"
}