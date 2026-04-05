package com.example.playlistmaker.search.ui.tracksRecycler

import androidx.recyclerview.widget.DiffUtil
import com.example.playlistmaker.search.domain.models.Track

class TracksDiffUtil: DiffUtil.ItemCallback<Track>() {
    override fun areItemsTheSame(
        oldItem: Track,
        newItem: Track
    ): Boolean {
        return oldItem.trackId == newItem.trackId
    }

    override fun areContentsTheSame(
        oldItem: Track,
        newItem: Track
    ): Boolean {
        return oldItem == newItem
    }
}