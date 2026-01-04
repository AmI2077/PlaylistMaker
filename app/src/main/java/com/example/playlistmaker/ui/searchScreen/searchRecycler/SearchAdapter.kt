package com.example.playlistmaker.ui.searchScreen.searchRecycler

import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.ListAdapter
import com.example.playlistmaker.data.models.Track

class SearchAdapter(
    val onItemClick: (track: Track) -> Unit
) : ListAdapter<Track, SearchViewHolder>(SearchDiffUtil()) {
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
        val view = holder.itemView
        view.setOnClickListener {
            onItemClick(getItem(position))
            Toast.makeText(view.context, "Добавлено в историю", Toast.LENGTH_SHORT).show()
        }
    }
}