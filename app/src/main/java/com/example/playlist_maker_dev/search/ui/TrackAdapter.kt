package com.example.playlist_maker_dev.search.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlist_maker_dev.search.domain.models.Track


class TrackAdapter(
    var tracks: List<Track>,
    private val onItemClick: (Track) -> Unit,
    private val onItemLongClick: (Track) -> Unit
) :
    RecyclerView.Adapter<TrackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder =
        TrackViewHolder(parent)

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            onItemClick(tracks[position])
        }
        holder.itemView.setOnLongClickListener {
            onItemLongClick(tracks[position])
            true
        }
    }

    override fun getItemCount(): Int = tracks.size
}

