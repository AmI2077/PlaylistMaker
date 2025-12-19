package com.example.playlistmaker.ui.searchRecycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.models.Track

class SearchRecyclerAdapter(val tracks: List<Track>) :
    RecyclerView.Adapter<SearchRecyclerViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchRecyclerViewHolder {
        return SearchRecyclerViewHolder(parent)
    }

    override fun onBindViewHolder(
        holder: SearchRecyclerViewHolder,
        position: Int
    ) {
        holder.bind(tracks[position])
    }

    override fun getItemCount(): Int {
        return tracks.size
    }
}