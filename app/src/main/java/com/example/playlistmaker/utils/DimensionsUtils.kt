package com.example.playlistmaker.utils

import android.content.Context
import android.util.TypedValue

class DimensionsUtils {
    companion object {
        fun dpToPixel(dp: Float, context: Context): Int {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                context.resources.displayMetrics
            ).toInt()
        }
    }
}