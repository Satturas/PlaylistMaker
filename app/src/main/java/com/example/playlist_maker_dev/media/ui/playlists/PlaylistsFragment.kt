package com.example.playlist_maker_dev.media.ui.playlists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class PlaylistsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val navController = findNavController()
        return ComposeView(requireContext()).apply {
            setContent {
                PlaylistsScreen(navController)
            }
        }
    }

    companion object {
        const val PLAYLIST_ID_KEY = "PLAYLIST_ID_KEY"
    }
}