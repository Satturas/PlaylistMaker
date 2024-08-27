package com.example.playlist_maker_dev.ui.search

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlist_maker_dev.domain.models.Track


class TrackAdapter(var tracks: List<Track>, private val searchActivity: SearchActivity) :
    RecyclerView.Adapter<TrackViewHolder>() {
    private var onClickListener: OnClickListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder =
        TrackViewHolder(parent)


    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {

            onClickListener?.onClick(position, tracks[position])
            searchActivity.updateHistoryOfTracksList(position)
        }
    }

    override fun getItemCount(): Int = tracks.size

    fun setOnClickListener(listener: OnClickListener?) {
        this.onClickListener = listener
    }

    interface OnClickListener {
        fun onClick(position: Int, track: Track)
    }
}
