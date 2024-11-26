package com.example.playlist_maker_dev.media.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.playlist_maker_dev.databinding.FragmentMediaFavoriteTracksBinding
import com.example.playlist_maker_dev.player.ui.AudioPlayerActivity
import com.example.playlist_maker_dev.search.domain.models.Track
import com.example.playlist_maker_dev.search.ui.SearchFragment.Companion.AUDIO_PLAYER
import com.example.playlist_maker_dev.search.ui.TrackAdapter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaFavoriteTracksFragment : Fragment() {

    private var _binding: FragmentMediaFavoriteTracksBinding? = null
    private val binding get() = _binding!!
    private val favouriteTracksList = mutableListOf<Track>()
    private var isClickAllowed = true

    private val viewModel by viewModel<FavoriteTracksViewModel>()

    private val adapter: TrackAdapter by lazy {
        TrackAdapter(mutableListOf()) { track ->
            handleTrackClick(
                track
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMediaFavoriteTracksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.mediaState.observe(viewLifecycleOwner) {
            render(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun render(state: MediaState) {
        when (state) {
            is MediaState.NothingInFavourite -> showNothingInFavourite()
            is MediaState.FavouriteTracks -> showFavouriteTracks(state.favouriteTracks)
        }
    }

    private fun showNothingInFavourite() {
        binding.placeholderImage.visibility = View.VISIBLE
        binding.placeholderMessage.visibility = View.VISIBLE
        binding.rvTracks.visibility = View.GONE
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showFavouriteTracks(favouriteTracks: List<Track>) {
        binding.placeholderImage.visibility = View.GONE
        binding.placeholderMessage.visibility = View.GONE
        adapter.tracks = favouriteTracks
        binding.rvTracks.adapter = adapter
        adapter.notifyDataSetChanged()
        binding.rvTracks.visibility = View.VISIBLE
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
            adapter.notifyDataSetChanged()
        }
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


    companion object {
        fun newInstance() = MediaFavoriteTracksFragment()
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}