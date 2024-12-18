package com.example.playlist_maker_dev.media.ui.playlists

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlist_maker_dev.media.domain.models.Playlist

class PlaylistAdapter(var playlists: List<Playlist>, private val onItemClick: (Playlist) -> Unit) :
    RecyclerView.Adapter<PlaylistsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistsViewHolder =
        PlaylistsViewHolder(parent)

    override fun onBindViewHolder(holder: PlaylistsViewHolder, position: Int) {
        holder.bind(playlists[position])
        holder.itemView.setOnClickListener {
            onItemClick(playlists[position])
        }
    }

    override fun getItemCount(): Int = playlists.size
}
