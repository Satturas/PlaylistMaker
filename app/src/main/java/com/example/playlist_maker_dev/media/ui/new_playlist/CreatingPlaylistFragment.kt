package com.example.playlist_maker_dev.media.ui.new_playlist

import android.app.AlertDialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.graphics.Color
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlist_maker_dev.R
import com.example.playlist_maker_dev.databinding.FragmentCreatingPlaylistBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.File
import java.io.FileOutputStream

class CreatingPlaylistFragment : Fragment() {

    private var _binding: FragmentCreatingPlaylistBinding? = null
    private val binding get() = _binding!!

    private var inputPlaylistNameValue: CharSequence = DEF_PLAYLIST_NAME
    private var inputPlaylistDescriptionValue: CharSequence = DEF_PLAYLIST_DESCRIPTION

    private lateinit var confirmDialog: MaterialAlertDialogBuilder

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreatingPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null) {
            inputPlaylistNameValue =
                savedInstanceState.getCharSequence(PLAYLIST_NAME_USER_INPUT, DEF_PLAYLIST_NAME)
            inputPlaylistDescriptionValue = savedInstanceState.getCharSequence(
                PLAYLIST_DESCRIPTION_USER_INPUT, DEF_PLAYLIST_DESCRIPTION
            )
        }

        binding.etFillPlaylistName.setText(inputPlaylistNameValue)
        binding.etFillPlaylistDescription.setText(inputPlaylistDescriptionValue)

        confirmDialog = MaterialAlertDialogBuilder(requireActivity(), R.style.AlertDialog)
            .setTitle("Завершить создание плейлиста?")
            .setMessage("Все несохраненные данные будут потеряны")
            .setNeutralButton("Отмена") { _, _ ->
            }.setNegativeButton("Завершить") { _, _ ->
                findNavController().navigateUp()
            }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (
                        binding.etFillPlaylistName.text.isEmpty()
                        && binding.etFillPlaylistDescription.text.isEmpty()
                        && binding.imageCover.drawable == null
                    ) {
                        findNavController().navigateUp()
                        return
                    }
                    confirmDialog.show()
                }
            }
        )

        binding.toolbar.setOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    binding.imageCover.setImageURI(uri)
                    saveImageToPrivateStorage(uri)
                }
            }

        binding.imageCover.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.createNewPlaylistButton.setOnClickListener { TODO() }

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


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun saveImageToPrivateStorage(uri: Uri) {
        val filePath =
            File(
                requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "playlist_covers_album"
            )
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        val file = File(filePath, "first_cover.jpg")
        val inputStream = requireActivity().contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
    }

    companion object {
        private const val PLAYLIST_NAME_USER_INPUT = "playlist_name_user_input"
        private const val PLAYLIST_DESCRIPTION_USER_INPUT = "playlist_description_user_input"
        private val DEF_PLAYLIST_NAME: CharSequence = ""
        private val DEF_PLAYLIST_DESCRIPTION: CharSequence = ""
    }
}