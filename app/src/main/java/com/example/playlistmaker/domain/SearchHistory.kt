package com.example.playlistmaker.domain

import com.example.playlistmaker.data.models.Track
import com.example.playlistmaker.data.repository.SearchHistoryRepository

class SearchHistory(
    private val repository: SearchHistoryRepository
) {

    fun getSearchHistory(): List<Track> {
        return repository.get()
    }
    fun saveSearchHistory(tracks: List<Track>) {
        repository.save(tracks)
    }
    fun clearSearchHistory() {
        repository.clear()
    }
    fun addTrackToHistory(
        track: Track,
        tracks: MutableList<Track>
    ): List<Track> {
        val iterator = tracks.iterator()
        var index: Int = -1
        while(iterator.hasNext()) {
            val item = iterator.next()
            if (item.trackId == track.trackId) {
                index = tracks.indexOf(item)
                iterator.remove()
                break
            }
        }
        if (tracks.size >= 10) {
            tracks.removeAt(tracks.lastIndex)
        }
        tracks.add(0, track)
        return tracks
    }
}