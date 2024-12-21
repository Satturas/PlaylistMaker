package com.example.playlist_maker_dev.media.ui.playlist_screen

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlist_maker_dev.R
import com.example.playlist_maker_dev.databinding.FragmentPlaylistBinding
import com.example.playlist_maker_dev.media.domain.models.Playlist
import com.example.playlist_maker_dev.media.ui.playlists.PlaylistsFragment.Companion.PLAYLIST_ID_KEY
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistScreenFragment : Fragment() {

    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!

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

        viewModel.playlist.observe(viewLifecycleOwner) { playlist ->
            showPlaylist(playlist)
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


    companion object {
        fun newInstance(id: Int) = PlaylistScreenFragment().apply {
            arguments = bundleOf(PLAYLIST_ID_KEY to id)
        }
    }
}