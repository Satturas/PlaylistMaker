package com.example.playlist_maker_dev.ui.search

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.playlist_maker_dev.creator.Creator
import com.example.playlist_maker_dev.R
import com.example.playlist_maker_dev.databinding.ActivitySearchBinding
import com.example.playlist_maker_dev.domain.api.TracksInteractor
import com.example.playlist_maker_dev.domain.models.Track
import com.example.playlist_maker_dev.player.ui.AudioPlayerActivity

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private var inputValue: CharSequence = SEARCH_DEF
    private val tracksList = mutableListOf<Track>()
    private var historyOfTracksList = mutableListOf<Track>()

    private val adapter: TrackAdapter by lazy {
        TrackAdapter(mutableListOf()) { track ->
            handleTrackClick(
                track
            )
        }
    }
    private val searchHistoryAdapter: TrackAdapter by lazy {
        TrackAdapter(mutableListOf()) { track ->
            handleTrackClick(
                track
            )
        }
    }
    private lateinit var inputEditTextSearchTracks: EditText
    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { findTrack() }
    private var isClickAllowed = true


    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        inputEditTextSearchTracks = findViewById(R.id.inputEditTextSearchTracks)

        val backFromSettingsButton = findViewById<Button>(R.id.buttonBackFromSearch)
        backFromSettingsButton.setOnClickListener {
            finish()
        }

        if (savedInstanceState != null) inputValue =
            savedInstanceState.getCharSequence(SEARCH_USER_INPUT, SEARCH_DEF)

        if (historyOfTracksList.isEmpty()) showSearchHistory(false)

        binding.inputEditTextSearchTracks.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && binding.inputEditTextSearchTracks.text.isNullOrEmpty()) {
                if (historyOfTracksList.isEmpty()) {
                    historyOfTracksList = Creator.provideSearchHistoryInteractor(this)
                        .getHistoryOfTracks()//getHistoryOfTracksUseCase.execute()
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
            binding.inputEditTextSearchTracks.setText(getString(R.string.emptyString))
            binding.inputEditTextSearchTracks.onEditorAction(EditorInfo.IME_ACTION_DONE)
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
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
            Creator.provideSearchHistoryInteractor(this).saveHistoryOfTracks(historyOfTracksList)
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
            Creator.provideSearchHistoryInteractor(this).getHistoryOfTracks()
        binding.rvHistorySearchTracks.adapter = searchHistoryAdapter

    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putCharSequence(SEARCH_USER_INPUT, inputValue)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        inputValue = savedInstanceState.getString(SEARCH_USER_INPUT).toString()
        binding.inputEditTextSearchTracks.setText(inputValue)
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

            val reloadButton = findViewById<Button>(R.id.buttonReload)
            reloadButton.setOnClickListener {
                showSearchProblemPlaceholders()
                findTrack()
            }

            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(
                binding.inputEditTextSearchTracks.windowToken, 0
            )

            tracksList.clear()
            adapter.notifyDataSetChanged()
            binding.placeholderMessage.text = text

            if (additionalMessage.isNotEmpty()) {
                Toast.makeText(applicationContext, additionalMessage, Toast.LENGTH_LONG).show()
            }

        } else {
            binding.placeholderMessage.visibility = View.GONE
            binding.placeholderImage.visibility = View.GONE
        }
    }

    private fun findTrack() {
        if (binding.inputEditTextSearchTracks.text.isNotEmpty()) {
            binding.progressBar.visibility = View.VISIBLE
            binding.searchHistoryTitle.visibility = View.GONE
            binding.rvHistorySearchTracks.visibility = View.GONE
            binding.buttonClearSearchHistory.visibility = View.GONE
            Creator.provideTracksInteractor(this).searchTracks(
                binding.inputEditTextSearchTracks.text.toString(),
                object : TracksInteractor.TracksConsumer {
                    @SuppressLint("NotifyDataSetChanged")
                    override fun consume(foundTracks: List<Track>) {
                        runOnUiThread {
                            binding.progressBar.visibility = View.GONE
                            tracksList.clear()
                            if (!foundTracks.isNullOrEmpty()) {
                                tracksList.addAll(foundTracks)
                                adapter.notifyDataSetChanged()
                            }
                            if (tracksList.isEmpty()) {
                                showMessage(
                                    getString(R.string.nothing_found),
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
                            getString(R.string.something_went_wrong),
                            t.message.toString(),
                            ErrorCauses.NO_CONNECTION
                        )
                        showSearchHistory(false)
                    }
                })
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

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

    @SuppressLint("NotifyDataSetChanged")
    private fun handleTrackClick(track: Track) {
        if (clickDebounce()) {
            val intent = Intent(this, AudioPlayerActivity::class.java).apply {
                putExtra(AUDIO_PLAYER, track)
            }
            Creator.provideSearchHistoryInteractor(this).saveTrackToHistory(track)
            searchHistoryAdapter.tracks =
                Creator.provideSearchHistoryInteractor(this).getHistoryOfTracks()
            searchHistoryAdapter.notifyDataSetChanged()
            startActivity(intent)
        }
    }

    enum class ErrorCauses {
        NO_CONNECTION, NO_RESULTS, EVERYTHING_GOOD
    }

    companion object {
        private const val SEARCH_USER_INPUT = "search_user_input"
        private val SEARCH_DEF: CharSequence = ""
        const val AUDIO_PLAYER = "track_for_player"
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}