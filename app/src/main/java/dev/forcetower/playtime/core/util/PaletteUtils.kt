package dev.forcetower.playtime.core.util

import android.graphics.Color
import androidx.core.graphics.ColorUtils
import androidx.palette.graphics.Palette

object PaletteUtils {
    fun Palette.getFirstNonBright(): Int {
        val vibrant = getVibrantColor(Color.WHITE)
        if (ColorUtils.calculateLuminance(vibrant) < 0.6) return vibrant

        val dominant = getDominantColor(Color.WHITE)
        if (ColorUtils.calculateLuminance(dominant) < 0.6) return dominant

        val muted = getMutedColor(Color.WHITE)
        if (ColorUtils.calculateLuminance(muted) < 0.6) return muted

        val darkV = getDarkVibrantColor(Color.WHITE)
        if (ColorUtils.calculateLuminance(darkV) < 0.6) return darkV

        val darkM = getDarkMutedColor(Color.WHITE)
        if (ColorUtils.calculateLuminance(darkM) < 0.6) return darkM

        return Color.BLACK
    }
}