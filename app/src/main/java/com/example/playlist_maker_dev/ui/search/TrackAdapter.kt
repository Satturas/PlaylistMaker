package com.example.playlist_maker_dev.ui.search

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlist_maker_dev.domain.models.Track


class TrackAdapter(var tracks: List<Track>, private val onItemClick: (Track) -> Unit) :
    RecyclerView.Adapter<TrackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder =
        TrackViewHolder(parent)


    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            onItemClick(tracks[position])
        }
    }

    override fun getItemCount(): Int = tracks.size
}
