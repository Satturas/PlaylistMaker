package com.example.playlist_maker_dev.media.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlist_maker_dev.databinding.FragmentMediaFavoriteTracksBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaFavoriteTracksFragment : Fragment() {

    private var _binding: FragmentMediaFavoriteTracksBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<FavoriteTracksViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMediaFavoriteTracksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = MediaFavoriteTracksFragment()
    }
}