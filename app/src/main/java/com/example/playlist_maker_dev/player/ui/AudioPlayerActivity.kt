package com.example.playlist_maker_dev.player.ui

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlist_maker_dev.R
import com.example.playlist_maker_dev.databinding.ActivityAudioPlayerBinding
import com.example.playlist_maker_dev.search.domain.models.Track
import com.example.playlist_maker_dev.search.ui.SearchFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAudioPlayerBinding

    private val viewModel by viewModel<AudioPlayerViewModel>()

    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAudioPlayerBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        val bottomSheetContainer = findViewById<LinearLayout>(R.id.playlists_bottom_sheet)

        val overlay = findViewById<View>(R.id.overlay)

        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {

                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        overlay.visibility = View.GONE
                    }

                    else -> {
                        overlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        binding.toolbar.setOnClickListener { finish() }

        val track = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra(SearchFragment.AUDIO_PLAYER, Track::class.java)
        } else {
            intent.getSerializableExtra(SearchFragment.AUDIO_PLAYER) as Track
        }

        showLoading()

        if (track != null) {
            showPlayer(track)
            viewModel.preparePlayer(track)
            viewModel.renderFavState(track)

            if (track.isFavorite) {
                binding.addToFavoriteButton.setImageResource(R.drawable.vector_added_favorite_button)
            } else {
                binding.addToFavoriteButton.setImageResource(R.drawable.vector_add_favorite_button)
            }
        }

        viewModel.playerState.observe(this) { playerState ->

            when (playerState) {

                AudioPlayerState.STATE_PREPARED -> {
                    binding.songTime.setText(R.string.timer_00)
                    binding.playButton.setImageResource(R.drawable.vector_play)
                }

                AudioPlayerState.STATE_PLAYING -> {
                    binding.playButton.setImageResource(R.drawable.vector_pause_button)

                }

                AudioPlayerState.STATE_PAUSED -> {
                    binding.playButton.setImageResource(R.drawable.vector_play)
                }

                else -> {}
            }

        }

        viewModel.currentSongTime.observe(this) { time ->
            binding.songTime.text = dateFormat.format(time)
        }


        viewModel.favouriteState.observe(this) {
            if (it) {
                binding.addToFavoriteButton.setImageResource(R.drawable.vector_add_favorite_button)
            } else {
                binding.addToFavoriteButton.setImageResource(R.drawable.vector_added_favorite_button)
            }
        }

        binding.playButton.setOnClickListener {
            if (viewModel.playerState.value == AudioPlayerState.STATE_PLAYING) {
                viewModel.pausePlayer()
            } else {
                viewModel.startPlayer()
            }
        }

        binding.addToFavoriteButton.setOnClickListener {
            if (track != null) {
                viewModel.onFavouriteClicked(track)
            }
        }
    }

    private fun showLoading() {
        binding.progressBar.isVisible = true
    }

    private fun showPlayer(track: Track) {
        binding.songTitle.text = track.trackName
        binding.bandTitle.text = track.artistName
        binding.songLength.text = track.trackTimeMillis
        binding.album.text = track.collectionName
        binding.album.visibility = View.VISIBLE
        binding.albumTitle.visibility = View.VISIBLE
        binding.year.text = track.releaseDate?.substring(0, 4) ?: ""
        binding.genre.text = track.primaryGenreName
        binding.country.text = track.country

        Glide
            .with(binding.imageCover)
            .load(track.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.vector_cover_placeholder)
            .centerCrop()
            .transform(RoundedCorners(dpToPx(2.0f, binding.imageCover.context)))
            .into(binding.imageCover)

        binding.progressBar.isVisible = false
    }

    fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    override fun onStop() {
        super.onStop()
        viewModel.stopPlayer()
    }
}