package com.example.playlistmaker.search.ui.tracksRecycler

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.playlistmaker.search.domain.models.Track

class TracksAdapter(
    val onItemClick: (track: Track) -> Unit
) : ListAdapter<Track, TracksViewHolder>(TracksDiffUtil()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TracksViewHolder {
        return TracksViewHolder(parent)
    }

    override fun onBindViewHolder(
        holder: TracksViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position), onItemClick)
    }
}