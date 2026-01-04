package com.example.playlistmaker.ui.searchScreen.searchHistoryRecycler

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.playlistmaker.data.models.Track
import com.example.playlistmaker.ui.searchScreen.searchRecycler.SearchViewHolder

class SearchHistoryAdapter: ListAdapter<Track, SearchViewHolder>(SearchHistoryDiffUtil()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchViewHolder {
        return SearchViewHolder(parent)
    }

    override fun onBindViewHolder(
        holder: SearchViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }
}