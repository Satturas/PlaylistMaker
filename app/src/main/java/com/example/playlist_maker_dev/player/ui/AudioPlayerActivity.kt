package com.example.playlist_maker_dev.player.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlist_maker_dev.R
import com.example.playlist_maker_dev.databinding.ActivityAudioPlayerBinding
import com.example.playlist_maker_dev.media.domain.models.Playlist
import com.example.playlist_maker_dev.media.ui.new_playlist.CreatingPlaylistFragment
import com.example.playlist_maker_dev.media.ui.playlists.PlaylistsState
import com.example.playlist_maker_dev.search.domain.models.Track
import com.example.playlist_maker_dev.search.ui.SearchFragment
import com.example.playlist_maker_dev.services.MusicService
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAudioPlayerBinding

    private val viewModel by viewModel<AudioPlayerViewModel>()

    private val playlistsList = mutableListOf<Playlist>()

    private val adapter: AddingToPlaylistAdapter by lazy {
        AddingToPlaylistAdapter(mutableListOf()) { playlist ->
            handlePlaylistClick(
                playlist
            )
        }
    }

    private lateinit var currentTrack: Track

    private val serviceConnection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MusicService.MusicServiceBinder
            viewModel.setAudioPlayerControl(binder.getService())
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            viewModel.removeAudioPlayerControl()
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (!isGranted) {
            Toast.makeText(this, "Can't start foreground service!", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAudioPlayerBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        viewModel.playerState.observe(this) {
            updateButtonAndProgress(it)
        }

        adapter.playlists = playlistsList
        binding.rvAddToPlaylist.adapter = adapter

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.playlistsBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {

                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.visibility = View.GONE
                    }

                    else -> {
                        binding.overlay.visibility = View.VISIBLE
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
            //viewModel.preparePlayer(track)
            viewModel.renderFavState(track)

            if (track.isFavorite) {
                binding.addToFavoriteButton.setImageResource(R.drawable.vector_added_favorite_button)
            } else {
                binding.addToFavoriteButton.setImageResource(R.drawable.vector_add_favorite_button)
            }
            currentTrack = track
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        bindMusicService()

        /*viewModel.playerState.observe(this) { playerState ->

            when (playerState) {
                PlayerState.Prepared() -> {
                    binding.songTime.setText(R.string.timer_00)
                    binding.customPlayButton.redraw(false)
                }

                PlayerState.Playing() -> {
                    binding.customPlayButton.redraw(true)
                }

                PlayerState.Paused() -> {
                    binding.customPlayButton.redraw(false)
                }

                else -> {}
            }

        }*/

        /*viewModel.currentSongTime.observe(this) { time ->
            binding.songTime.text = dateFormat.format(time)
        }*/

        viewModel.favouriteState.observe(this) {
            if (it) {
                binding.addToFavoriteButton.setImageResource(R.drawable.vector_add_favorite_button)
            } else {
                binding.addToFavoriteButton.setImageResource(R.drawable.vector_added_favorite_button)
            }
        }

        binding.customPlayButton.setOnClickListener {
            viewModel.onPlayerButtonClicked()
        }

        binding.addToFavoriteButton.setOnClickListener {
            if (track != null) {
                viewModel.onFavouriteClicked(track)
            }
        }

        binding.addPlaylistButton.setOnClickListener {
            if (track != null) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                viewModel.onAddToPlaylistClicked()
                viewModel.playlistsState.observe(this) {
                    render(it)
                }
            }
        }

        binding.createNewPlaylistButton.setOnClickListener {
            supportFragmentManager.commit {
                replace<CreatingPlaylistFragment>(R.id.playerFragmentContainerView)
                addToBackStack(null)
                setReorderingAllowed(true)
            }
            BottomSheetBehavior.from(binding.playlistsBottomSheet).apply {
                state = BottomSheetBehavior.STATE_HIDDEN
            }
        }
    }

    /*override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    override fun onStop() {
        super.onStop()
        viewModel.stopPlayer()
    }*/

    override fun onDestroy() {
        unbindMusicService()
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        viewModel.showNotification(false)
    }

    override fun onPause() {
        super.onPause()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                viewModel.showNotification(true)
            }
        } else {
            viewModel.showNotification(true)
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

    private fun render(state: PlaylistsState) {
        when (state) {
            is PlaylistsState.NoPlaylists -> {}
            is PlaylistsState.FoundPlaylistsContent -> showPlaylists(state.foundPlaylists)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showPlaylists(foundPlaylists: List<Playlist>) {
        playlistsList.clear()
        playlistsList.addAll(foundPlaylists)
        adapter.playlists = foundPlaylists
        binding.rvAddToPlaylist.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun handlePlaylistClick(playlist: Playlist) {
        if (viewModel.trackIsInPlaylist(currentTrack, playlist)) {
            Toast.makeText(
                this,
                "Трек уже добавлен в плейлист ${playlist.name}",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            viewModel.addTrackToPlaylist(currentTrack, playlist)
            Toast.makeText(
                this,
                "Добавлено в плейлист ${playlist.name}",
                Toast.LENGTH_SHORT
            ).show()
            BottomSheetBehavior.from(binding.playlistsBottomSheet).state =
                BottomSheetBehavior.STATE_HIDDEN
        }
        adapter.notifyDataSetChanged()
    }

    private fun bindMusicService() {
        val intent = Intent(this, MusicService::class.java).apply {
            putExtra("track_url", currentTrack.previewUrl)
            putExtra("track_title", currentTrack.trackName)
            putExtra("artist_name", currentTrack.artistName)
        }
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    private fun unbindMusicService() {
        unbindService(serviceConnection)
    }

    private fun updateButtonAndProgress(playerState: PlayerState) {
        binding.customPlayButton.redraw(playerState.buttonState)
        binding.songTime.text = playerState.progress

    }
}