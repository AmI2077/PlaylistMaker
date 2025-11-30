package com.example.playlistmaker.searchRecycler

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.models.Track

class SearchViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.song_item_view, parent, false)
) {
    private val trackNameView: TextView = itemView.findViewById<TextView>(R.id.track_name)
    private val artistNameView: TextView = itemView.findViewById<TextView>(R.id.artist_name)
    private val trackTimeView: TextView = itemView.findViewById<TextView>(R.id.track_time)
    private val trackArtworkView: ImageView = itemView.findViewById<ImageView>(R.id.artwork)

    fun bind(model: Track) {
        trackNameView.text = model.trackName
        artistNameView.text = model.artistName
        trackTimeView.text = model.trackTime
        setupImage(model, trackArtworkView)
    }

    fun setupImage(model: Track, imageView: ImageView) {
        Glide
            .with(itemView)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.placeholder_45)
            .centerCrop()
            .transform(RoundedCorners(10))
            .into(imageView)
    }
}