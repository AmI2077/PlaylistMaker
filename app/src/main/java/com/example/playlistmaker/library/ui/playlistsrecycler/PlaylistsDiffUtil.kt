package com.example.playlistmaker.library.ui.playlistsrecycler

import androidx.recyclerview.widget.DiffUtil
import com.example.playlistmaker.library.domain.model.Playlist

class PlaylistsDiffUtil(): DiffUtil.ItemCallback<Playlist>() {
    override fun areItemsTheSame(
        oldItem: Playlist,
        newItem: Playlist
    ): Boolean {
        return oldItem.title == newItem.title
    }

    override fun areContentsTheSame(
        oldItem: Playlist,
        newItem: Playlist
    ): Boolean {
        return oldItem == newItem
    }
}