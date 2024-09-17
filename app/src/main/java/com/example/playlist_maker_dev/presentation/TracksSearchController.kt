package com.example.playlist_maker_dev.presentation

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.playlist_maker_dev.R
import com.example.playlist_maker_dev.creator.Creator
import com.example.playlist_maker_dev.databinding.ActivitySearchBinding
import com.example.playlist_maker_dev.search.domain.models.Track
import com.example.playlist_maker_dev.search.ui.TrackAdapter

class TracksSearchController(
    private val activity: Activity,
    private val adapter: TrackAdapter,
    private val searchHistoryAdapter: TrackAdapter
) {
    //private val tracksInteractor = Creator.provideTracksInteractor(activity)

    private lateinit var binding: ActivitySearchBinding
    private val tracksList = mutableListOf<Track>()
    private var historyOfTracksList = mutableListOf<Track>()
    private var inputValue: CharSequence = SEARCH_DEF
    private val handler = Handler(Looper.getMainLooper())
    //private val searchRunnable = Runnable { findTrack() }

    private lateinit var inputEditTextSearchTracks: EditText

    @SuppressLint("NotifyDataSetChanged")
    fun onCreate() {

        binding = ActivitySearchBinding.inflate(LayoutInflater.from(activity))

        inputEditTextSearchTracks = activity.findViewById(R.id.inputEditTextSearchTracks)

        if (historyOfTracksList.isEmpty()) showSearchHistory(false)

        binding.inputEditTextSearchTracks.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && binding.inputEditTextSearchTracks.text.isNullOrEmpty()) {
                if (historyOfTracksList.isEmpty()) {
                    historyOfTracksList = Creator.provideSearchHistoryInteractor()
                        .getHistoryOfTracks()
                    searchHistoryAdapter.tracks = historyOfTracksList
                    searchHistoryAdapter.notifyDataSetChanged()
                    showHistoryByEmptyOrNotList()
                }
            } else
                showSearchHistory(false)
        }

        binding.inputEditTextSearchTracks.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.searchDeleteButton.visibility =
                    if (binding.inputEditTextSearchTracks.hasFocus() && p0?.isEmpty() == true) View.VISIBLE else View.GONE
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        binding.searchDeleteButton.setOnClickListener {
            binding.inputEditTextSearchTracks.setText(activity.getString(R.string.emptyString))
            binding.inputEditTextSearchTracks.onEditorAction(EditorInfo.IME_ACTION_DONE)
            val inputMethodManager =
                activity.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(
                binding.searchDeleteButton.windowToken,
                0
            )
            showSearchProblemPlaceholders()
            tracksList.clear()
            adapter.notifyDataSetChanged()
            showHistoryByEmptyOrNotList()
        }

        binding.buttonClearSearchHistory.setOnClickListener {
            historyOfTracksList.clear()
            searchHistoryAdapter.notifyDataSetChanged()
            Creator.provideSearchHistoryInteractor()
                .saveHistoryOfTracks(historyOfTracksList)
            showSearchHistory(false)
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.searchDeleteButton.isVisible = clearButtonVisibility(s)
                if (s?.isEmpty() == true) {
                    tracksList.clear()
                    adapter.notifyDataSetChanged()
                    showSearchHistory(historyOfTracksList.isNotEmpty())
                }
                searchDebounce()
            }

            override fun afterTextChanged(s: Editable?) {
                inputValue = s.toString()
            }
        }
        binding.inputEditTextSearchTracks.addTextChangedListener(simpleTextWatcher)

        adapter.tracks = tracksList
        binding.rvTracks.adapter = adapter

        searchHistoryAdapter.tracks =
            Creator.provideSearchHistoryInteractor().getHistoryOfTracks()
        binding.rvHistorySearchTracks.adapter = searchHistoryAdapter

    }

    fun onDestroy() {
        //handler.removeCallbacks(searchRunnable)
    }

    private fun searchDebounce() {
        //handler.removeCallbacks(searchRunnable)
        //handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }


    private fun clearButtonVisibility(s: CharSequence?): Boolean = !s.isNullOrEmpty()

    @SuppressLint("NotifyDataSetChanged")
    private fun showMessage(text: String, additionalMessage: String, cause: ErrorCauses) {
        if (text.isNotEmpty()) {
            when (cause) {
                ErrorCauses.NO_RESULTS -> binding.placeholderImage.setImageResource(R.drawable.vector_nothing_found)

                ErrorCauses.NO_CONNECTION -> {
                    binding.placeholderImage.setImageResource(R.drawable.vector_search_no_internet)
                    binding.buttonReload.visibility = View.VISIBLE
                }

                else -> {}
            }
            binding.placeholderMessage.visibility = View.VISIBLE
            binding.placeholderImage.visibility = View.VISIBLE

            val reloadButton = activity.findViewById<Button>(R.id.buttonReload)
            reloadButton.setOnClickListener {
                showSearchProblemPlaceholders()
                //findTrack()
            }

            val inputMethodManager =
                activity.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(
                binding.inputEditTextSearchTracks.windowToken, 0
            )

            tracksList.clear()
            adapter.notifyDataSetChanged()
            binding.placeholderMessage.text = text

            if (additionalMessage.isNotEmpty()) {
                Toast.makeText(activity.applicationContext, additionalMessage, Toast.LENGTH_LONG)
                    .show()
            }

        } else {
            binding.placeholderMessage.visibility = View.GONE
            binding.placeholderImage.visibility = View.GONE
        }
    }

    /*private fun findTrack() {
        if (binding.inputEditTextSearchTracks.text.isNotEmpty()) {
            binding.progressBar.visibility = View.VISIBLE
            binding.searchHistoryTitle.visibility = View.GONE
            binding.rvHistorySearchTracks.visibility = View.GONE
            binding.buttonClearSearchHistory.visibility = View.GONE
            Creator.provideTracksInteractor(activity).searchTracks(
                binding.inputEditTextSearchTracks.text.toString(),
                object : TracksInteractor.TracksConsumer {
                    @SuppressLint("NotifyDataSetChanged")
                    override fun consume(foundTracks: List<Track>) {
                        activity.runOnUiThread {
                            binding.progressBar.visibility = View.GONE
                            tracksList.clear()
                            if (!foundTracks.isNullOrEmpty()) {
                                tracksList.addAll(foundTracks)
                                adapter.notifyDataSetChanged()
                            }
                            if (tracksList.isEmpty()) {
                                showMessage(
                                    activity.getString(R.string.nothing_found),
                                    "",
                                    ErrorCauses.NO_RESULTS
                                )
                                showSearchHistory(false)
                            } else {
                                showMessage("", "", ErrorCauses.EVERYTHING_GOOD)
                            }
                        }
                    }

                    override fun onFailure(t: Throwable) {
                        binding.progressBar.visibility = View.GONE
                        showMessage(
                            activity.getString(R.string.something_went_wrong),
                            t.message.toString(),
                            ErrorCauses.NO_CONNECTION
                        )
                        showSearchHistory(false)
                    }
                })
        }
    }*/


    private fun showSearchHistory(isVisible: Boolean) {
        if (isVisible) {
            binding.searchHistoryTitle.visibility = View.VISIBLE
            binding.rvHistorySearchTracks.visibility = View.VISIBLE
            binding.buttonClearSearchHistory.visibility = View.VISIBLE
        } else {
            binding.searchHistoryTitle.visibility = View.GONE
            binding.rvHistorySearchTracks.visibility = View.GONE
            binding.buttonClearSearchHistory.visibility = View.GONE
        }
    }

    private fun showHistoryByEmptyOrNotList() {
        if (historyOfTracksList.isEmpty())
            showSearchHistory(false)
        else
            showSearchHistory(true)
    }

    private fun showSearchProblemPlaceholders() {
        binding.placeholderImage.visibility = View.GONE
        binding.buttonReload.visibility = View.GONE
        binding.placeholderMessage.visibility = View.GONE
    }


    enum class ErrorCauses {
        NO_CONNECTION, NO_RESULTS, EVERYTHING_GOOD
    }

    companion object {
        const val AUDIO_PLAYER = "track_for_player"
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private val SEARCH_DEF: CharSequence = ""
    }
}
