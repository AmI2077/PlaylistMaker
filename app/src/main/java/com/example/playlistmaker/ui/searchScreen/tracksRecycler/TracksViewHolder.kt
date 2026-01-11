package com.example.playlistmaker.ui.searchScreen.tracksRecycler

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.data.model.Track
import com.example.playlistmaker.utils.DimensionsUtils
import java.text.SimpleDateFormat
import java.util.Locale

class TracksViewHolder(
    parent: ViewGroup,
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.track_item_view, parent, false)
) {

    val trackNameView: TextView = itemView.findViewById<TextView>(R.id.track_name)
    val artistNameView: TextView = itemView.findViewById<TextView>(R.id.artist_name)
    val trackTimeView: TextView = itemView.findViewById<TextView>(R.id.track_time)
    val trackArtView: ImageView = itemView.findViewById<ImageView>(R.id.track_artwork)


    fun bind(model: Track, onItemClick: (track: Track) -> Unit) {
        itemView.setOnClickListener {
            onItemClick(model)
        }
        trackNameView.text = model.trackName
        artistNameView.text = model.artistName
        val duration = model.trackTimeMillis.toLong()
        trackTimeView.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(duration)
        setImage(model.artworkUrl100)
    }

    fun setImage(url: String) {
        Glide.with(itemView)
            .load(url)
            .centerCrop()
            .transform(RoundedCorners(DimensionsUtils.dpToPixel(2f, itemView.context)))
            .placeholder(R.drawable.ic_track_placeholder_45)
            .into(trackArtView)
    }

}