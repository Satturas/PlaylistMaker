package com.example.playlist_maker_dev.media.ui.new_playlist

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlist_maker_dev.R
import com.example.playlist_maker_dev.databinding.FragmentCreatingPlaylistBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

open class CreatingPlaylistFragment : Fragment() {

    private var _binding: FragmentCreatingPlaylistBinding? = null
    protected val binding get() = _binding!!

    private var inputPlaylistNameValue: CharSequence = DEF_PLAYLIST_NAME
    private var inputPlaylistDescriptionValue: CharSequence = DEF_PLAYLIST_DESCRIPTION

    private lateinit var confirmDialog: MaterialAlertDialogBuilder

    private val viewModel by viewModel<CreatingPlaylistViewModel>()

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
                requireActivity().supportFragmentManager.popBackStack()
            }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (
                        binding.etFillPlaylistName.text?.isBlank() == true
                        && binding.etFillPlaylistDescription.text?.isBlank() == true
                        && binding.imageCover.drawable == null
                    ) {
                        requireActivity().supportFragmentManager.popBackStack()
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
        }

        binding.createNewPlaylistButton.setOnClickListener {
            viewModel.createPlaylist(
                binding.etFillPlaylistName.text.toString(),
                binding.etFillPlaylistDescription.text.toString()
            )
            Toast.makeText(
                requireContext(),
                "Плейлист ${binding.etFillPlaylistName.text} создан",
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