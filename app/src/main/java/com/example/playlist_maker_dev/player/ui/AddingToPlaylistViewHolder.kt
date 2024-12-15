package com.example.playlist_maker_dev.player.ui

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlist_maker_dev.R
import com.example.playlist_maker_dev.media.domain.models.Playlist

class AddingToPlaylistViewHolder(parent: ViewGroup) :
    RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.adding_to_playlist_item, parent, false)
    ) {

    private val tvPlaylistName: TextView = itemView.findViewById(R.id.tv_playlist_name)
    private val tvNumberOfTracks: TextView = itemView.findViewById(R.id.tv_number_of_tracks)
    private val ivPlaylistCover: ImageView = itemView.findViewById(R.id.iv_playlist_cover)

    fun bind(item: Playlist) {
        Glide
            .with(itemView)
            .load(item.coverUrl)
            .placeholder(R.drawable.vector_cover_placeholder)
            .centerCrop()
            .transform(RoundedCorners(dpToPx(2.0f, itemView.context)))
            .into(ivPlaylistCover)
        tvPlaylistName.text = item.name
        tvNumberOfTracks.text = numberOfTracks(item.tracksQuantity)
    }
}

fun dpToPx(dp: Float, context: Context): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        context.resources.displayMetrics
    ).toInt()
}

fun numberOfTracks(number: Int): String {
    var tempNumber = number % 100
    if (tempNumber in 5..20) {
        return "$number треков"
    } else {
        tempNumber = number % 10
        return when (tempNumber) {
            in 1..1 -> "$number трек"
            in 2..4 -> "$number трека"
            else -> "$number треков"
        }
    }
}