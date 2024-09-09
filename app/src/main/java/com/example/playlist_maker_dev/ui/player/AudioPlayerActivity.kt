package com.example.playlist_maker_dev.ui.player

import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlist_maker_dev.creator.Creator
import com.example.playlist_maker_dev.R
import com.example.playlist_maker_dev.databinding.ActivityAudioPlayerBinding
import com.example.playlist_maker_dev.domain.api.AudioPlayerInteractor
import com.example.playlist_maker_dev.domain.models.Track
import com.example.playlist_maker_dev.ui.search.SearchActivity
import com.example.playlist_maker_dev.ui.search.dpToPx

class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAudioPlayerBinding
    private var mediaPlayer = MediaPlayer()
    private lateinit var url: String
    private lateinit var audioPlayerInteractor: AudioPlayerInteractor
    private lateinit var mainThreadHandler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAudioPlayerBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        val backButton = findViewById<Toolbar>(R.id.toolbar)
        backButton.setOnClickListener {
            finish()
        }

        val track = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra(SearchActivity.AUDIO_PLAYER, Track::class.java)
        } else {
            intent.getSerializableExtra(SearchActivity.AUDIO_PLAYER) as Track
        }

        if (track != null) {
            binding.songTitle.text = track.trackName
            binding.bandTitle.text = track.artistName
            binding.songLength.text = track.trackTimeMillis

            if (track.collectionName?.isNotEmpty() == true) {
                binding.album.text = track.collectionName
                binding.album.visibility = View.VISIBLE
                binding.albumTitle.visibility = View.VISIBLE
            }

            if (track.releaseDate?.isNotEmpty() == true) {
                binding.year.text = track.releaseDate.substring(0, 4)
            }
            binding.genre.text = track.primaryGenreName
            binding.country.text = track.country
            Glide
                .with(binding.imageCover)
                .load(track.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg"))
                .placeholder(R.drawable.vector_cover_placeholder)
                .centerCrop()
                .transform(RoundedCorners(dpToPx(2.0f, binding.imageCover.context)))
                .into(binding.imageCover)

            url = track.previewUrl
        }

        mainThreadHandler = Handler(Looper.getMainLooper())

        audioPlayerInteractor = Creator.provideAudioPlayerInteractor(
            mediaPlayer,
            mainThreadHandler,
            track,
            binding.playButton,
            binding.songTime
        )
    }

    override fun onPause() {
        super.onPause()
        audioPlayerInteractor.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        mainThreadHandler.removeCallbacks(audioPlayerInteractor.setCurrentPosition())
    }

}
