package com.example.playlist_maker_dev.media.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlist_maker_dev.databinding.FragmentMediaFavoriteTracksBinding

class MediaFavoriteTracksFragment : Fragment() {

    private lateinit var binding: FragmentMediaFavoriteTracksBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMediaFavoriteTracksBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        fun newInstance() = MediaFavoriteTracksFragment()
    }
}