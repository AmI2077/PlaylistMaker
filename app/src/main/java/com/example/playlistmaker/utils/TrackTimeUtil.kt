package com.example.playlistmaker.utils

import java.text.SimpleDateFormat
import java.util.Locale

class TrackTimeUtil {
    companion object {
        fun formatTrackTime(duration: String): String {
            val time = duration.toLong()
            return SimpleDateFormat("mm:ss", Locale.getDefault()).format(time)
        }
    }
}