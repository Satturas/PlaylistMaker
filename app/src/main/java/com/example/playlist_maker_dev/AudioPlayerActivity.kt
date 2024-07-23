package com.example.playlist_maker_dev

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlist_maker_dev.databinding.ActivityAudioPlayerBinding
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAudioPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAudioPlayerBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        val backButton = findViewById<Toolbar>(R.id.toolbar)
        backButton.setOnClickListener {
            finish()
        }

        val emptyTrack: Track?

        if (intent.hasExtra(SearchActivity.AUDIO_PLAYER)) {

            emptyTrack = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getSerializableExtra(SearchActivity.AUDIO_PLAYER, Track::class.java)
            } else {
                intent.getSerializableExtra(SearchActivity.AUDIO_PLAYER) as Track
            }

            if (emptyTrack != null) {
                binding.songTitle.text = emptyTrack.trackName
                binding.songLength.text = SimpleDateFormat(
                    "mm:ss",
                    Locale.getDefault()
                ).format(emptyTrack.trackTimeMillis.toLong())

                if (emptyTrack.collectionName?.isNotEmpty() == true) {
                    binding.bandTitle.text = emptyTrack.collectionName
                    binding.album.text = emptyTrack.collectionName
                    binding.bandTitle.visibility = View.VISIBLE
                    binding.album.visibility = View.VISIBLE
                    binding.albumTitle.visibility = View.VISIBLE
                }

                if (emptyTrack.releaseDate?.isNotEmpty() == true) {
                    binding.year.text = emptyTrack.releaseDate.substring(0, 4)
                }
                binding.genre.text = emptyTrack.primaryGenreName
                binding.country.text = emptyTrack.country
                Glide
                    .with(binding.imageCover)
                    .load(emptyTrack.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg"))
                    .placeholder(R.drawable.vector_cover_placeholder)
                    .centerCrop()
                    .transform(RoundedCorners(dpToPx(2.0f, binding.imageCover.context)))
                    .into(binding.imageCover)
            }
        }
    }
}
