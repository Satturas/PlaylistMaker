package com.example.playlist_maker_dev.media.ui.playlist_screen

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlist_maker_dev.R
import com.example.playlist_maker_dev.databinding.FragmentPlaylistBinding
import com.example.playlist_maker_dev.media.domain.models.Playlist
import com.example.playlist_maker_dev.media.ui.playlists.PlaylistAdapter
import com.example.playlist_maker_dev.media.ui.playlists.PlaylistsFragment.Companion.PLAYLIST_ID_KEY
import com.example.playlist_maker_dev.player.ui.AudioPlayerActivity
import com.example.playlist_maker_dev.search.domain.models.Track
import com.example.playlist_maker_dev.search.ui.SearchFragment.Companion.AUDIO_PLAYER
import com.example.playlist_maker_dev.search.ui.TrackAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistScreenFragment : Fragment() {

    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!
    private val tracksList = mutableListOf<Track>()
    private var isClickAllowed = true
    private var playlistId: Int = 0

    /*private val adapter: TrackAdapter by lazy {
        TrackAdapter(mutableListOf(), { track ->
            handleTrackClick(
                track
            )
        }) { track ->
            handleTrackLongClick(
                track
            )
        }
    }*/

    private val viewModel by viewModel<PlaylistScreenViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getPlaylistById(requireArguments().getInt(PLAYLIST_ID_KEY))
        viewModel.getTracks(requireArguments().getInt(PLAYLIST_ID_KEY))

        viewModel.playlist.observe(viewLifecycleOwner) { playlist ->
            playlistId = playlist.id
            showPlaylist(playlist)
        }

        viewModel.playlistTracks.observe(viewLifecycleOwner) { tracks ->
            showTracks(tracks)
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().supportFragmentManager.popBackStack()
                    return
                }
            }
        )

        binding.toolbar.setOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }

        /*adapter.tracks = tracksList
        binding.rvTracksList.adapter = adapter*/
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }

    private fun showPlaylist(playlist: Playlist) {
        Glide
            .with(binding.imageCover)
            .load(playlist.coverUrl)
            .placeholder(R.drawable.vector_cover_placeholder)
            .centerCrop()
            .transform(RoundedCorners(dpToPx(2.0f, binding.imageCover.context)))
            .into(binding.imageCover)
        binding.playlistName.text = playlist.name
        binding.playlistDescription.text = playlist.description
        binding.tvPlaylistTracksQuantity.text = numberOfTracks(playlist.tracksQuantity)
        binding.tvTotalPlaylistLength.text = lengthOfTracks(playlist.tracksLength)
    }

    private fun numberOfTracks(number: Int): String {
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

    private fun lengthOfTracks(number: Int): String {
        var tempNumber = number % 100
        if (tempNumber in 5..20) {
            return "$number минут"
        } else {
            tempNumber = number % 10
            return when (tempNumber) {
                in 1..1 -> "$number минута"
                in 2..4 -> "$number минуты"
                else -> "$number минут"
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun handleTrackClick(track: Track) {
        if (clickDebounce()) {
            val intent = Intent(
                requireContext(),
                AudioPlayerActivity::class.java
            ).apply {
                putExtra(AUDIO_PLAYER, track)
            }
            startActivity(intent)
        }
    }

    private fun handleTrackLongClick(track: Track) {
        MaterialAlertDialogBuilder(requireActivity(), R.style.AlertDialog)
            .setTitle("Удалить трек")
            .setMessage("Вы уверены, что хотите удалить трек из плейлиста?")
            .setNeutralButton("Отмена") { _, _ ->
            }.setNegativeButton("Удалить") { _, _ ->
                viewModel.removeTrackFromPlaylist(track.trackId, playlistId)
            }.show()
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showTracks(tracks: List<Track>) {
        val adapter = TrackAdapter(tracks, { track ->
            handleTrackClick(
                track
            )
        }) { track ->
            handleTrackLongClick(
                track
            )

        }

        binding.rvTracksList.adapter = adapter
        adapter.notifyDataSetChanged()


        /*tracksList.clear()
        tracksList.addAll(tracks)
        adapter.tracks = tracks
        binding.rvTracksList.adapter = adapter
        adapter.notifyDataSetChanged()*/
    }


    companion object {
        fun newInstance(id: Int) = PlaylistScreenFragment().apply {
            arguments = bundleOf(PLAYLIST_ID_KEY to id)
        }

        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}