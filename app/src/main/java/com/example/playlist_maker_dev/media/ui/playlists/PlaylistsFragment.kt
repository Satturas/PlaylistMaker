package com.example.playlist_maker_dev.media.ui.playlists

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlist_maker_dev.R
import com.example.playlist_maker_dev.databinding.FragmentMediaPlaylistsBinding
import com.example.playlist_maker_dev.media.domain.models.Playlist
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {

    private var _binding: FragmentMediaPlaylistsBinding? = null
    private val binding get() = _binding!!
    private var isClickAllowed = true

    private val viewModel by viewModel<PlaylistsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMediaPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.createNewPlaylistButton.setOnClickListener {
            findNavController().navigate(R.id.creatingPlaylistFragment)
        }

        viewModel.playlistsState.observe(viewLifecycleOwner) {
            render(it)
        }

        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        viewModel.showPlaylists()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        viewModel.showPlaylists()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun handlePlaylistClick(playlist: Playlist) {
        if (clickDebounce()) {
            findNavController().navigate(R.id.playlistScreenFragment, Bundle().apply {
                putInt(
                    PLAYLIST_ID_KEY, playlist.id
                )
            })
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
            }
            isClickAllowed = true
        }
        return current
    }

    private fun render(state: PlaylistsState) {
        when (state) {
            is PlaylistsState.NoPlaylists -> showNoPlaylists()
            is PlaylistsState.FoundPlaylistsContent -> showPlaylists(state.foundPlaylists)
        }
    }

    private fun showNoPlaylists() {
        binding.recyclerView.visibility = View.GONE
        binding.placeholderImage.visibility = View.VISIBLE
        binding.placeholderMessage.visibility = View.VISIBLE
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showPlaylists(playlists: List<Playlist>) {
        binding.recyclerView.visibility = View.VISIBLE
        binding.placeholderImage.visibility = View.GONE
        binding.placeholderMessage.visibility = View.GONE
        val adapter = PlaylistAdapter(playlists) { playlist ->
            handlePlaylistClick(
                playlist
            )
        }
        binding.recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    companion object {
        fun newInstance() = PlaylistsFragment()
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        const val PLAYLIST_ID_KEY = "PLAYLIST_ID_KEY"
    }
}