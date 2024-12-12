package com.example.playlist_maker_dev.media.ui

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

class MediaPlaylistsFragment : Fragment() {

    private var _binding: FragmentMediaPlaylistsBinding? = null
    private val binding get() = _binding!!
    private val playlistsList = mutableListOf<Playlist>()
    private var isClickAllowed = true

    private val adapter: PlaylistAdapter by lazy {
        PlaylistAdapter(mutableListOf()) { playlist ->
            handlePlaylistClick(
                playlist
            )
        }
    }

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
            findNavController().navigate(R.id.action_mediaPlaylistsFragment_to_creatingPlaylistFragment)
        }

        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        adapter.playlists = playlistsList
        binding.recyclerView.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun handlePlaylistClick(playlist: Playlist) {
        if (clickDebounce()) {
            TODO()
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
        fun newInstance() = MediaPlaylistsFragment()
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}