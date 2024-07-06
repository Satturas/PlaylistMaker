package com.example.playlist_maker_dev

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TrackAdapter(var tracks: List<Track>) : RecyclerView.Adapter<TrackViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder =
        TrackViewHolder(parent)


    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
        val searchActivity = SearchActivity()
        holder.itemView.setOnClickListener { searchActivity.updateHistoryOfTracksList(position) }
    }

    override fun getItemCount(): Int = tracks.size
}
