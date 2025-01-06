package com.example.playlist_maker_dev.media.ui.playlist_screen

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlist_maker_dev.R
import com.example.playlist_maker_dev.databinding.FragmentPlaylistBinding
import com.example.playlist_maker_dev.media.domain.models.Playlist
import com.example.playlist_maker_dev.media.ui.playlists.PlaylistsFragment.Companion.PLAYLIST_ID_KEY
import com.example.playlist_maker_dev.player.ui.AudioPlayerActivity
import com.example.playlist_maker_dev.search.domain.models.Track
import com.example.playlist_maker_dev.search.ui.SearchFragment.Companion.AUDIO_PLAYER
import com.example.playlist_maker_dev.search.ui.TrackAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistScreenFragment : Fragment() {

    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!
    private var isClickAllowed = true
    private var playlistId: Int = 0
    private var currentPlaylist: Playlist? = null
    private var playlistTracksLength: Int = 0
    private var playlistTracksNumber: Int = 0

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
        binding.rvTracksList.layoutManager = LinearLayoutManager(requireContext())

        viewModel.playlist.observe(viewLifecycleOwner) { playlist ->
            currentPlaylist = playlist
            playlistId = playlist.id
            playlistTracksLength = playlist.tracksLength
            playlistTracksNumber = playlist.tracksQuantity
            showPlaylist(playlist)

            if (playlistTracksNumber == 0) {
                binding.placeholderImage.visibility = View.VISIBLE
                binding.placeholderMessage.visibility = View.VISIBLE
            } else {
                binding.placeholderImage.visibility = View.GONE
                binding.placeholderMessage.visibility = View.GONE
            }
        }

        viewModel.playlistTracks.observe(viewLifecycleOwner) { tracks ->
            showTracks(tracks)
            Log.e("tracks", tracks.toString())
        }

        binding.shareButton.setOnClickListener {
            share()
        }

        val bottomSheetShareBehavior =
            BottomSheetBehavior.from(binding.playlistShareBottomSheet).apply {
                state = BottomSheetBehavior.STATE_HIDDEN
            }

        bottomSheetShareBehavior.addBottomSheetCallback(object :
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

        binding.detailsButton.setOnClickListener {
            Log.e("error", currentPlaylist.toString())
            currentPlaylist?.let { it1 -> showPlaylistInfoSmall(it1) }
            bottomSheetShareBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

            binding.tvPlaylistShare.setOnClickListener {
                share()
            }

            binding.tvPlaylistDelete.setOnClickListener {
                bottomSheetShareBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                MaterialAlertDialogBuilder(requireActivity(), R.style.AlertDialog)
                    .setTitle("Удалить плейлист")
                    .setMessage("Хотите удалить плейлист?")
                    .setNegativeButton("Нет") { _, _ ->
                    }.setPositiveButton("Да") { _, _ ->
                        viewModel.removePlaylist(playlistId)
                        findNavController().navigate(R.id.mediaFragment)
                        val bottomPanel =
                            requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
                        if (bottomPanel != null)
                            bottomPanel.visibility = View.VISIBLE
                    }.show()

            }

            binding.tvPlaylistEditInfo.setOnClickListener {
                findNavController().navigate(
                    R.id.editPlaylistFragment,
                    Bundle().apply { putInt(PLAYLIST_ID_KEY, playlistId) }
                )
            }
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val mainLayout = requireActivity().findViewById<ConstraintLayout>(R.id.main_layout)
        if (mainLayout != null)
            mainLayout.visibility = View.GONE
        val bottomPanel =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        if (bottomPanel != null)
            bottomPanel.visibility = View.GONE
    }

    override fun onDetach() {
        super.onDetach()
        val mainLayout = requireActivity().findViewById<ConstraintLayout>(R.id.main_layout)
        if (mainLayout != null)
            mainLayout.visibility = View.VISIBLE
        val bottomPanel =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        if (bottomPanel != null)
            bottomPanel.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        val bottomPanel =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        if (bottomPanel != null)
            bottomPanel.visibility = View.GONE
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
        binding.tvPlaylistTracksQuantity.text = numberOfTracks(playlistTracksNumber)
        binding.tvTotalPlaylistLength.text = lengthOfTracks(playlistTracksLength)
    }

    private fun showPlaylistInfoSmall(playlist: Playlist) {
        Glide
            .with(binding.ivPlaylistCoverSmall)
            .load(playlist.coverUrl)
            .placeholder(R.drawable.vector_cover_placeholder)
            .centerCrop()
            .transform(RoundedCorners(dpToPx(2.0f, binding.ivPlaylistCoverSmall.context)))
            .into(binding.ivPlaylistCoverSmall)
        binding.tvPlaylistNameSmall.text = playlist.name
        binding.tvNumberOfTracksSmall.text = numberOfTracks(playlistTracksNumber)
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

    private fun share() {
        when (playlistTracksNumber) {
            0 -> Toast.makeText(
                requireActivity(),
                "В этом плейлисте нет списка треков, которым можно поделиться",
                Toast.LENGTH_SHORT
            ).show()

            else -> viewModel.sharePlaylistToOtherApps(playlistId)
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
                binding.tvPlaylistTracksQuantity.text = numberOfTracks(playlistTracksNumber - 1)
                binding.tvTotalPlaylistLength.text = lengthOfTracks(
                    playlistTracksLength - track.trackTimeMillis.substring(0, 2).toInt()
                )
                playlistTracksLength -= track.trackTimeMillis.substring(0, 2).toInt()
                playlistTracksNumber -= 1
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
    }


    companion object {
        fun newInstance(id: Int) = PlaylistScreenFragment().apply {
            arguments = bundleOf(PLAYLIST_ID_KEY to id)
        }

        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}