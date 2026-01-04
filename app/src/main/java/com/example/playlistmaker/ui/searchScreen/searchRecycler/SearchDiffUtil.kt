package com.example.playlistmaker.ui.searchScreen.searchRecycler

import androidx.recyclerview.widget.DiffUtil
import com.example.playlistmaker.data.models.Track

class SearchDiffUtil: DiffUtil.ItemCallback<Track>() {
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
        return oldItem == newItem
    }
}