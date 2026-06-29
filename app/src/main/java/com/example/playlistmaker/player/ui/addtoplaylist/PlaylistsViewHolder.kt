package com.example.playlistmaker.player.ui.addtoplaylist

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistHorizontalItemBinding
import com.example.playlistmaker.library.domain.model.Playlist
import com.example.playlistmaker.utils.DimensionsUtils

class PlaylistsViewHolder(private val binding: PlaylistHorizontalItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Playlist, onItemClick: (playlist: Playlist) -> Unit) {
        binding.root.setOnClickListener {
            onItemClick(item)
        }

        binding.playlistTitle.text = item.title
        setImage(item.artworkUri)
        binding.tracksCount.text = binding.root.resources.getQuantityString(
            R.plurals.tracksCountInPlaylist,
            item.countTracks,
            item.countTracks
        )
    }

    private fun setImage(imagePath: String?) {
        val radius = DimensionsUtils.dpToPixel(2f, binding.root.context)
        Glide
            .with(binding.root.context)
            .load(imagePath)
            .transform(CenterCrop(), RoundedCorners(radius))
            .placeholder(R.drawable.ic_track_placeholder_45)
            .into(binding.playlistArtwork)
    }
}