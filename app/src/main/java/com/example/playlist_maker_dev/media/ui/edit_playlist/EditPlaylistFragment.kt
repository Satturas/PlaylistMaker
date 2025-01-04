package com.example.playlist_maker_dev.media.ui.edit_playlist

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlist_maker_dev.R
import com.example.playlist_maker_dev.media.domain.models.Playlist
import com.example.playlist_maker_dev.media.ui.new_playlist.CreatingPlaylistFragment
import com.example.playlist_maker_dev.media.ui.playlists.PlaylistsFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditPlaylistFragment : CreatingPlaylistFragment() {

    private var inputPlaylistNameValue: CharSequence = DEF_PLAYLIST_NAME
    private var inputPlaylistDescriptionValue: CharSequence = DEF_PLAYLIST_DESCRIPTION
    private lateinit var currentPlaylist: Playlist
    private var isUriChanged = false

    private val viewModel by viewModel<EditPlaylistViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.title = requireActivity().resources.getString(R.string.edit)
        binding.createNewPlaylistButton.text = requireActivity().resources.getString(R.string.save)

        if (savedInstanceState != null) {
            inputPlaylistNameValue =
                savedInstanceState.getCharSequence(PLAYLIST_NAME_USER_INPUT, DEF_PLAYLIST_NAME)
            inputPlaylistDescriptionValue = savedInstanceState.getCharSequence(
                PLAYLIST_DESCRIPTION_USER_INPUT, DEF_PLAYLIST_DESCRIPTION
            )
        }
        binding.etFillPlaylistName.setText(inputPlaylistNameValue)
        binding.etFillPlaylistDescription.setText(inputPlaylistDescriptionValue)
        binding.imageCover.setBackgroundResource(R.drawable.editing_playlist_cover_background)

        binding.toolbar.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        viewModel.getPlaylistById(requireArguments().getInt(PlaylistsFragment.PLAYLIST_ID_KEY))

        viewModel.playlist.observe(viewLifecycleOwner) { playlist ->
            Glide
                .with(this)
                .load(playlist.coverUrl)
                .placeholder(R.drawable.vector_cover_placeholder)
                .centerCrop()
                .transform(RoundedCorners(dpToPx(8.0f, binding.imageCover.context)))
                .into(binding.imageCover)

            currentPlaylist = playlist
            binding.etFillPlaylistName.setText(playlist.name)
            binding.etFillPlaylistDescription.setText(playlist.description)
        }

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    Glide
                        .with(this)
                        .load(uri)
                        .placeholder(R.drawable.vector_cover_placeholder)
                        .centerCrop()
                        .transform(RoundedCorners(dpToPx(8.0f, binding.imageCover.context)))
                        .into(binding.imageCover)
                    viewModel.saveImageToPrivateStorage(uri)
                }
            }

        binding.imageCover.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            isUriChanged = true
        }

        binding.createNewPlaylistButton.setOnClickListener {
            val currentUri = if (isUriChanged) "-1" else currentPlaylist.coverUrl.toString()
            viewModel.updatePlaylist(
                currentPlaylist.id,
                binding.etFillPlaylistName.text.toString(),
                binding.etFillPlaylistDescription.text.toString(),
                currentUri,
                currentPlaylist.tracksQuantity,
                currentPlaylist.tracksLength
            )
            Toast.makeText(
                requireContext(),
                "Плейлист ${binding.etFillPlaylistName.text} изменен",
                Toast.LENGTH_SHORT
            ).show()
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.etFillPlaylistName.doOnTextChanged { text, _, _, _ ->
            binding.createNewPlaylistButton.isEnabled = text.isNullOrBlank().not()
        }

        binding.etFillPlaylistDescription.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                inputPlaylistDescriptionValue = p0.toString()
            }
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putCharSequence(PLAYLIST_NAME_USER_INPUT, inputPlaylistNameValue)
        outState.putCharSequence(PLAYLIST_DESCRIPTION_USER_INPUT, inputPlaylistDescriptionValue)
    }

    private fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }

    companion object {
        private const val PLAYLIST_NAME_USER_INPUT = "playlist_name_user_input"
        private const val PLAYLIST_DESCRIPTION_USER_INPUT = "playlist_description_user_input"
        private val DEF_PLAYLIST_NAME: CharSequence = ""
        private val DEF_PLAYLIST_DESCRIPTION: CharSequence = ""
    }
}
