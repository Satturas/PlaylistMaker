package com.example.playlist_maker_dev

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TrackViewHolder (itemView: View): RecyclerView.ViewHolder(itemView) {

    private val rootLayout: LinearLayout = itemView.findViewById(R.id.rootLayout)
    private val tvTrack: TextView = itemView.findViewById(R.id.tvSong)
    private val tvArtist: TextView = itemView.findViewById(R.id.tvBand)
    private val tvLength: TextView = itemView.findViewById(R.id.tvLength)
    private val ivCover: ImageView = itemView.findViewById(R.id.ivCover)

    fun bind(item: Track) {
        //val trackName = item.trackName
        //val artistName = item.artistName
        //val trackTime = item.trackTime
        //val artworkUrl100 = item.artworkUrl100 //когда нет интернета, сюда заглушку!
        val coverIcon = R.drawable.vector_cover_placeholder

        tvTrack.text = item.trackName
        tvArtist.text = item.artistName
        tvLength.text = item.trackTime
        ivCover.setImageResource(coverIcon)
      }
}