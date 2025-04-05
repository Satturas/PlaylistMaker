package com.example.playlist_maker_dev.media.ui.media_fav_tracks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.example.playlist_maker_dev.search.domain.models.Track
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaFavoriteTracksFragment : Fragment() {

    //private var _binding: FragmentMediaFavoriteTracksBinding? = null
    // private val binding get() = _binding!!
    private val favouriteTracksList = mutableListOf<Track>()

    // private var isClickAllowed = true
    private val viewModel by viewModel<FavoriteTracksViewModel>()

    /* private val adapter: TrackAdapter by lazy {
         TrackAdapter(mutableListOf(), { track ->
             handleTrackClick(
                 track
             )
         })

         { track ->
             handleTrackClick(
                 track
             )
         }
     }*/


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        //_binding = FragmentMediaFavoriteTracksBinding.inflate(inflater, container, false)
        //return binding.root
        return ComposeView(requireContext()).apply {
            setContent {
                MediaFavoriteTracksScreen()
            }
        }
    }

    /*override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.mediaState.observe(viewLifecycleOwner) {
            render(it)
        }

        adapter.tracks = favouriteTracksList
        binding.rvFavTracks.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        viewModel.fillData()
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
        binding.rvFavTracks.visibility = View.GONE
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showFavouriteTracks(favouriteTracks: List<Track>) {
        binding.placeholderImage.visibility = View.GONE
        binding.placeholderMessage.visibility = View.GONE
        favouriteTracksList.clear()
        favouriteTracksList.addAll(favouriteTracks)
        adapter.tracks = favouriteTracks
        binding.rvFavTracks.adapter = adapter
        adapter.notifyDataSetChanged()
        binding.rvFavTracks.visibility = View.VISIBLE
    }

    @SuppressLint("NotifyDataSetChanged")
    fun handleTrackClick(track: Track) {
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
    }*/
}