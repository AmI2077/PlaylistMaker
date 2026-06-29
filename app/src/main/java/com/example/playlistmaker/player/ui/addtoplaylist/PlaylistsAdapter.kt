package com.example.playlistmaker.player.ui.addtoplaylist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.playlistmaker.databinding.PlaylistHorizontalItemBinding
import com.example.playlistmaker.library.domain.model.Playlist
import com.example.playlistmaker.library.ui.playlistsrecycler.PlaylistsDiffUtil

class PlaylistsAdapter(
    private val onItemClick: (playlist: Playlist) -> Unit
): ListAdapter<Playlist, PlaylistsViewHolder>(PlaylistsDiffUtil()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlaylistsViewHolder {
        val binding = PlaylistHorizontalItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaylistsViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: PlaylistsViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position)) {
            onItemClick(it)
        }
    }
}