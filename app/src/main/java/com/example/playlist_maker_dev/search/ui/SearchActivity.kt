package com.example.playlist_maker_dev.search.ui

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
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.playlist_maker_dev.R
import com.example.playlist_maker_dev.databinding.ActivitySearchBinding
import com.example.playlist_maker_dev.player.ui.AudioPlayerActivity
import com.example.playlist_maker_dev.search.domain.models.Track
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding

    private var inputValue: CharSequence = SEARCH_DEF
    private val tracksList = mutableListOf<Track>()
    private var historyOfTracksList = mutableListOf<Track>()
    private lateinit var textWatcher: TextWatcher

    private val viewModel by viewModel<SearchViewModel>()

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
    private lateinit var queryInput: EditText
    private val handler = Handler(Looper.getMainLooper())
    private var isClickAllowed = true


    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        viewModel.showHistoryOfTracks()

        queryInput = binding.inputEditTextSearchTracks

        viewModel.searchState.observe(this) {
            render(it)
        }

        binding.buttonBackFromSearch.setOnClickListener { finish() }

        binding.searchDeleteButton.setOnClickListener {
            queryInput.setText(R.string.emptyString)
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
            viewModel.showHistoryOfTracks()
            hideSearchProblemPlaceholders(true)
            binding.rvTracks.visibility = View.GONE
            binding.progressBar.visibility = View.GONE
        }

        binding.inputEditTextSearchTracks.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.searchDeleteButton.visibility =
                    if (queryInput.hasFocus() && p0?.isEmpty() == true) View.VISIBLE else View.GONE
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        binding.inputEditTextSearchTracks.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && binding.inputEditTextSearchTracks.text.isNullOrEmpty()) {
                if (historyOfTracksList.isEmpty()) {
                    searchHistoryAdapter.tracks = historyOfTracksList
                    searchHistoryAdapter.notifyDataSetChanged()
                    showHistoryByEmptyOrNotList()
                }
            }
        }

        binding.buttonClearSearchHistory.setOnClickListener {
            historyOfTracksList.clear()
            searchHistoryAdapter.notifyDataSetChanged()
            viewModel.saveHistoryOfTracks(historyOfTracksList)
            hideSearchHistory(true)
        }

        if (savedInstanceState != null) inputValue =
            savedInstanceState.getCharSequence(SEARCH_USER_INPUT, SEARCH_DEF)

        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.searchDeleteButton.isVisible = clearButtonVisibility(s)
                viewModel.searchDebounce(changedText = s?.toString() ?: "")
            }

            override fun afterTextChanged(s: Editable?) {
                inputValue = s.toString()
            }
        }

        textWatcher.let { queryInput.addTextChangedListener(it) }

        adapter.tracks = tracksList
        binding.rvTracks.adapter = adapter

    }

    private fun render(state: SearchState) {
        when (state) {
            is SearchState.NothingFound -> showNothingFound()
            is SearchState.Loading -> showLoading()
            is SearchState.SearchHistoryTracksContent -> showSearchHistory(state.searchHistoryTracks)
            is SearchState.FoundTracksContent -> showTracks(state.foundTracks)
            is SearchState.Error -> showNetworkError()
        }
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

    private fun showNothingFound() {
        binding.progressBar.visibility = View.GONE
        hideSearchHistory(true)
        binding.placeholderImage.setImageResource(R.drawable.vector_nothing_found)
        hideSearchProblemPlaceholders(false)
        binding.buttonReload.visibility = View.GONE
    }

    private fun showLoading() {
        hideSearchHistory(true)
        hideSearchProblemPlaceholders(true)
        binding.progressBar.visibility = View.VISIBLE
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showSearchHistory(searchHistoryTracks: List<Track>) {
        binding.progressBar.visibility = View.GONE
        if (searchHistoryTracks.isNullOrEmpty()) {
            hideSearchHistory(true)
        } else {
            hideSearchHistory(false)
            hideSearchProblemPlaceholders(true)
            searchHistoryAdapter.tracks = searchHistoryTracks
            binding.rvHistorySearchTracks.adapter = searchHistoryAdapter
            searchHistoryAdapter.notifyDataSetChanged()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showTracks(foundTracks: List<Track>) {
        hideSearchHistory(true)
        hideSearchProblemPlaceholders(true)
        binding.rvTracks.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
        tracksList.clear()
        tracksList.addAll(foundTracks)
        adapter.tracks = foundTracks
        binding.rvTracks.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun showNetworkError() {
        hideSearchHistory(true)
        binding.progressBar.visibility = View.GONE
        binding.placeholderImage.setImageResource(R.drawable.vector_search_no_internet)
        binding.buttonReload.visibility = View.VISIBLE
        hideSearchProblemPlaceholders(false)
        binding.buttonReload.setOnClickListener {
            hideSearchProblemPlaceholders(true)
            viewModel.searchDebounce(inputValue.toString())
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

    private fun hideSearchHistory(isVisible: Boolean) {
        if (!isVisible) {
            binding.searchHistoryTitle.visibility = View.VISIBLE
            binding.rvHistorySearchTracks.visibility = View.VISIBLE
            binding.buttonClearSearchHistory.visibility = View.VISIBLE
        } else {
            binding.searchHistoryTitle.visibility = View.GONE
            binding.rvHistorySearchTracks.visibility = View.GONE
            binding.buttonClearSearchHistory.visibility = View.GONE
        }
    }

    private fun showHistoryByEmptyOrNotList() = hideSearchHistory(historyOfTracksList.isEmpty())

    private fun hideSearchProblemPlaceholders(isVisible: Boolean) {
        if (isVisible) {
            binding.placeholderImage.visibility = View.GONE
            binding.buttonReload.visibility = View.GONE
            binding.placeholderMessage.visibility = View.GONE
        } else {
            binding.placeholderImage.visibility = View.VISIBLE
            binding.buttonReload.visibility = View.VISIBLE
            binding.placeholderMessage.visibility = View.VISIBLE
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun handleTrackClick(track: Track) {
        if (clickDebounce()) {
            val intent = Intent(this, AudioPlayerActivity::class.java).apply {
                putExtra(AUDIO_PLAYER, track)
            }
            viewModel.saveTracktoHistory(track)
            startActivity(intent)
            searchHistoryAdapter.notifyDataSetChanged()
            viewModel.showHistoryOfTracks()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        textWatcher.let { queryInput.removeTextChangedListener(it) }
    }

    companion object {
        private const val SEARCH_USER_INPUT = "search_user_input"
        private val SEARCH_DEF: CharSequence = ""
        const val AUDIO_PLAYER = "track_for_player"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}




