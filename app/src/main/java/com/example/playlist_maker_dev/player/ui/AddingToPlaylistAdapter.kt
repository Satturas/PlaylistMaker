package com.example.playlist_maker_dev.player.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlist_maker_dev.media.domain.models.Playlist

class AddingToPlaylistAdapter(
    var playlists: List<Playlist>,
    private val onItemClick: (Playlist) -> Unit
) :
    RecyclerView.Adapter<AddingToPlaylistViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddingToPlaylistViewHolder =
        AddingToPlaylistViewHolder(parent)

    override fun onBindViewHolder(holder: AddingToPlaylistViewHolder, position: Int) {
        holder.bind(playlists[position])
        holder.itemView.setOnClickListener {
            onItemClick(playlists[position])
        }
    }

    override fun getItemCount(): Int = playlists.size
}