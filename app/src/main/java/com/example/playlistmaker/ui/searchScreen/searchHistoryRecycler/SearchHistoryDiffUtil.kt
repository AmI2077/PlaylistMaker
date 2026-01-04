package com.example.playlistmaker.ui.searchScreen.searchHistoryRecycler

import androidx.recyclerview.widget.DiffUtil
import com.example.playlistmaker.data.models.Track

class SearchHistoryDiffUtil: DiffUtil.ItemCallback<Track>() {
    override fun areItemsTheSame(
        oldItem: Track,
        newItem: Track
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: Track,
        newItem: Track
    ): Boolean {
        return oldItem.trackId == newItem.trackId
    }
}