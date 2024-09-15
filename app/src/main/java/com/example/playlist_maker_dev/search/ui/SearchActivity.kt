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
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.playlist_maker_dev.R
import com.example.playlist_maker_dev.databinding.ActivitySearchBinding
import com.example.playlist_maker_dev.player.ui.AudioPlayerActivity
import com.example.playlist_maker_dev.presentation.App
import com.example.playlist_maker_dev.search.domain.models.Track

class SearchActivity : ComponentActivity() {

    private lateinit var binding: ActivitySearchBinding

    private var inputValue: CharSequence = SEARCH_DEF
    private val tracksList = mutableListOf<Track>()
    private var historyOfTracksList = mutableListOf<Track>()
    private lateinit var textWatcher: TextWatcher

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            SearchViewModel.getViewModelFactory(application as App)
        )[SearchViewModel::class.java]
    }

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

    //private val searchRunnable =
    //    Runnable { viewModel.searchTracks(binding.inputEditTextSearchTracks.text.toString()) }
    private var isClickAllowed = true


    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        historyOfTracksList = viewModel.getHistoryOfTracks()

        if (historyOfTracksList.isNullOrEmpty()) {
            hideSearchHistory(true)
        }

        queryInput = binding.inputEditTextSearchTracks

        viewModel.getState().observe(this) {
            render(it)
        }

        binding.buttonBackFromSearch.setOnClickListener { finish() }

        binding.searchDeleteButton.setOnClickListener {
            queryInput.setText(R.string.emptyString)
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)

            if (viewModel.getHistoryOfTracks().isNullOrEmpty()) {
                hideSearchHistory(true)
            } else {
                hideSearchHistory(false)
            }
            hideSearchProblemPlaceholders()
            binding.rvTracks.visibility = View.GONE
            binding.progressBar.visibility = View.GONE
        }

        /*binding.searchDeleteButton.setOnClickListener {
            queryInput.setText(getString(R.string.emptyString))
            queryInput.onEditorAction(EditorInfo.IME_ACTION_DONE)
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(
                binding.searchDeleteButton.windowToken,
                0
            )
            //showSearchProblemPlaceholders()
            tracksList.clear()
            adapter.notifyDataSetChanged()
            viewModel.showHistoryOfTracks()
            //showHistoryByEmptyOrNotList()
        }*/

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
                    //viewModel.showHistoryOfTracks()
                    showHistoryByEmptyOrNotList()
                }
            }
        }
        //} else
        //showSearchHistory(false)


        //inputEditTextSearchTracks = findViewById(R.id.inputEditTextSearchTracks)


        //if (historyOfTracksList.isEmpty()) showSearchHistory(false)


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
                /* if (s?.isEmpty() == true) {
                     tracksList.clear()
                     adapter.notifyDataSetChanged()
                     //showSearchHistory(historyOfTracksList.isNotEmpty())
                     //viewModel.showHistoryOfTracks()
                 }*/
                viewModel.searchDebounce(changedText = s?.toString() ?: "")
            }

            override fun afterTextChanged(s: Editable?) {
                inputValue = s.toString()
            }
        }

        textWatcher?.let { queryInput.addTextChangedListener(it) }

        //binding.inputEditTextSearchTracks.addTextChangedListener(simpleTextWatcher)

        adapter.tracks = tracksList
        binding.rvTracks.adapter = adapter

        searchHistoryAdapter.tracks = viewModel.getHistoryOfTracks()
        //Creator.provideSearchHistoryInteractor(this).getHistoryOfTracks()
        binding.rvHistorySearchTracks.adapter = searchHistoryAdapter

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

    /*private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }*/

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
    /*private fun showMessage(text: String, additionalMessage: String, cause: ErrorCauses) {
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

            /*val reloadButton = findViewById<Button>(R.id.buttonReload)
            reloadButton.setOnClickListener {
                hideSearchProblemPlaceholders()
                findTrack()
            }*/

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
    }*/

    /*private fun findTrack() {
        if (binding.inputEditTextSearchTracks.text.isNotEmpty()) {
            binding.progressBar.visibility = View.VISIBLE
            binding.searchHistoryTitle.visibility = View.GONE
            binding.rvHistorySearchTracks.visibility = View.GONE
            binding.buttonClearSearchHistory.visibility = View.GONE
            Creator.provideTracksInteractor(this).searchTracks(
                binding.inputEditTextSearchTracks.text.toString(),
                object : TracksInteractor.TracksConsumer {
                    @SuppressLint("NotifyDataSetChanged")
                    override fun consume(foundTracks: List<Track>?, errorMessage: String?) {
                        runOnUiThread {
                            binding.progressBar.visibility = View.GONE
                            tracksList.clear()
                            if (!foundTracks.isNullOrEmpty()) {
                                tracksList.addAll(foundTracks)
                                adapter.notifyDataSetChanged()
                            }
                            if (errorMessage != null) {
                                binding.progressBar.visibility = View.GONE
                                showMessage(
                                    getString(R.string.something_went_wrong),
                                    "",
                                    ErrorCauses.NO_CONNECTION
                                )
                                showSearchHistory(false)
                            } else if (tracksList.isEmpty()) {
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

                    /*override fun onFailure(t: Throwable) {
                        binding.progressBar.visibility = View.GONE
                        showMessage(
                            getString(R.string.something_went_wrong),
                            t.message.toString(),
                            ErrorCauses.NO_CONNECTION
                        )
                        showSearchHistory(false)
                    }*/
                })
        }
    }*/

    private fun showNothingFound() {
        hideSearchHistory(true)
        binding.placeholderImage.setImageResource(R.drawable.vector_nothing_found)
    }

    private fun showLoading() {
        hideSearchHistory(true)
        hideSearchProblemPlaceholders()
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun showSearchHistory(searchHistoryTracks: List<Track>) {
        binding.progressBar.visibility = View.GONE
        if (searchHistoryTracks.isNullOrEmpty()) {
            hideSearchHistory(true)
        } else {
            hideSearchHistory(false)
            hideSearchProblemPlaceholders()
            //viewModel.showHistoryOfTracks()
            tracksList.clear()
            tracksList.addAll(searchHistoryTracks)
            adapter.notifyDataSetChanged()
        }
    }

    private fun showTracks(foundTracks: List<Track>) {
        hideSearchHistory(true)
        hideSearchProblemPlaceholders()
        binding.rvTracks.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
        tracksList.clear()
        tracksList.addAll(foundTracks)
        adapter.notifyDataSetChanged()
    }

    private fun showNetworkError() {
        hideSearchHistory(true)
        binding.progressBar.visibility = View.GONE
        binding.placeholderImage.setImageResource(R.drawable.vector_search_no_internet)
        binding.buttonReload.visibility = View.VISIBLE
        binding.buttonReload.setOnClickListener {
            viewModel.searchDebounce(binding.inputEditTextSearchTracks.text.toString())
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


    private fun hideSearchProblemPlaceholders() {
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
            viewModel.saveTracktoHistory(track)
            searchHistoryAdapter.tracks = viewModel.getHistoryOfTracks()
            //Creator.provideSearchHistoryInteractor(this).getHistoryOfTracks()
            searchHistoryAdapter.notifyDataSetChanged()
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        textWatcher?.let { queryInput.removeTextChangedListener(it) }
    }

    /*enum class ErrorCauses {
        NO_CONNECTION, NO_RESULTS, EVERYTHING_GOOD
    }*/

    companion object {
        private const val TRACK_ID = "track_id"
        private const val SEARCH_USER_INPUT = "search_user_input"
        private val SEARCH_DEF: CharSequence = ""
        const val AUDIO_PLAYER = "track_for_player"
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}




