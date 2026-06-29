package com.example.playlistmaker.library.ui.playlistsrecycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.playlistmaker.databinding.PlaylistItemViewBinding
import com.example.playlistmaker.library.domain.model.Playlist

class PlaylistsAdapter(private val onItemClick: (playlistId: Int) -> Unit) :
    ListAdapter<Playlist, PlaylistsViewHolder>(PlaylistsDiffUtil()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlaylistsViewHolder {
        val binding =
            PlaylistItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaylistsViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: PlaylistsViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position), onItemClick)
    }
}