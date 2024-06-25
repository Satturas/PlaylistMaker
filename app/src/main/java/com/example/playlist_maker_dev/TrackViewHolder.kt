package com.example.playlist_maker_dev

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewHolder(parent: ViewGroup) :
    RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.track_item, parent, false)
    ) {

    private val tvTrack: TextView = itemView.findViewById(R.id.tvSong)
    private val tvArtist: TextView = itemView.findViewById(R.id.tvBand)
    private val tvLength: TextView = itemView.findViewById(R.id.tvLength)
    private val ivCover: ImageView = itemView.findViewById(R.id.ivCover)

    fun bind(item: Track) {
        Glide
            .with(itemView)
            .load(item.artworkUrl100)
            .centerCrop()
            .placeholder(R.drawable.vector_cover_placeholder)
            .transform(RoundedCorners(dpToPx(2.0f, itemView.context)))
            .into(ivCover)

        tvTrack.text = item.trackName
        tvArtist.text = item.artistName
        tvLength.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(item.trackTimeMillis.toLong())
    }
}

private fun dpToPx(dp: Float, context: Context): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        context.resources.displayMetrics
    ).toInt()
}
