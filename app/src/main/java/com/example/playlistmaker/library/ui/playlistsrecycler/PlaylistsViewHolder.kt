package com.example.playlistmaker.library.ui.playlistsrecycler

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistItemViewBinding
import com.example.playlistmaker.library.domain.model.Playlist
import com.example.playlistmaker.utils.DimensionsUtils

class PlaylistsViewHolder(private val binding: PlaylistItemViewBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Playlist, onItemClick: (playlistId: Int) -> Unit) {
        binding.root.setOnClickListener {
            onItemClick(item.id)
        }
        binding.playlistTitle.text = item.title
        setImage(item.artworkUri)
        binding.tracksCount.text = binding.root.context.resources.getQuantityString(
            R.plurals.tracksCountInPlaylist,
            item.countTracks,
            item.countTracks,
        )
    }

    fun setImage(uri: String?) {
        val radius = DimensionsUtils.dpToPixel(8f, binding.root.context)

        Glide.with(binding.root)
            .load(uri)
            .placeholder(R.drawable.ic_playlist_placeholder_160)
            .transform(CenterCrop(), RoundedCorners(radius))
            .into(binding.playlistArtWork)
    }
}