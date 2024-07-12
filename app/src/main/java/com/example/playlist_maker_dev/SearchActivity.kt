package com.example.playlist_maker_dev

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
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
import com.example.playlist_maker_dev.databinding.ActivitySearchBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private val iTunesBaseUrl = "https://itunes.apple.com"
    private lateinit var binding: ActivitySearchBinding

    private val retrofit =
        Retrofit.Builder().baseUrl(iTunesBaseUrl).addConverterFactory(GsonConverterFactory.create())
            .build()

    private val iTunesService = retrofit.create(ITunesApi::class.java)
    private var inputValue: CharSequence = SEARCH_DEF
    private val tracksList = mutableListOf<Track>()
    private var historyOfTracksList = mutableListOf<Track>()
    private val adapter = TrackAdapter(mutableListOf(), this)
    private val searchHistoryAdapter = TrackAdapter(mutableListOf(), this)
    private lateinit var searchHistory: SearchHistory
    private lateinit var inputEditTextSearchTracks: EditText

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

        searchHistory = SearchHistory(
            getSharedPreferences(
                SEARCH_TRACKS_HISTORY, MODE_PRIVATE
            )
        )

        if (savedInstanceState != null) inputValue =
            savedInstanceState.getCharSequence(SEARCH_USER_INPUT, SEARCH_DEF)

        if (historyOfTracksList.isEmpty()) showSearchHistory(false)

        binding.inputEditTextSearchTracks.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && binding.inputEditTextSearchTracks.text.isNullOrEmpty()) {
                if (historyOfTracksList.isEmpty()) {
                    historyOfTracksList = searchHistory.readFromSharedPreferences()
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
            searchHistory.writeToSharedPreferences(historyOfTracksList)
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
            }

            override fun afterTextChanged(s: Editable?) {
                inputValue = s.toString()
            }
        }
        binding.inputEditTextSearchTracks.addTextChangedListener(simpleTextWatcher)

        adapter.tracks = tracksList
        binding.rvTracks.adapter = adapter

        searchHistoryAdapter.tracks = historyOfTracksList
        binding.rvHistorySearchTracks.adapter = searchHistoryAdapter

        binding.inputEditTextSearchTracks.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                findTrack()
            }
            true
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateHistoryOfTracksList(position: Int) {
        if (!inputEditTextSearchTracks.text.isNullOrEmpty()) {
            if (historyOfTracksList.isNotEmpty()) {
                historyOfTracksList.removeIf { it.trackId == tracksList[position].trackId }
                if (historyOfTracksList.size >= 10) {
                    historyOfTracksList.removeLast()
                }
            }
            historyOfTracksList.add(0, tracksList[position])
            searchHistoryAdapter.notifyDataSetChanged()
            searchHistory.writeToSharedPreferences(historyOfTracksList)
        } else {
            if (historyOfTracksList.isNotEmpty()) {
                val tempTrack = historyOfTracksList[position]
                historyOfTracksList.removeAt(position)
                historyOfTracksList.add(0, tempTrack)
                searchHistoryAdapter.notifyDataSetChanged()
                searchHistory.writeToSharedPreferences(historyOfTracksList)
            }
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
            iTunesService.findTrack(binding.inputEditTextSearchTracks.text.toString())
                .enqueue(object : Callback<TrackResponse> {
                    @SuppressLint("NotifyDataSetChanged")
                    override fun onResponse(
                        call: Call<TrackResponse>, response: Response<TrackResponse>
                    ) {
                        if (response.code() == 200) {
                            tracksList.clear()
                            if (!response.body()?.results.isNullOrEmpty()) {
                                tracksList.addAll(response.body()?.results ?: emptyList())
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
                        } else {
                            showMessage(
                                getString(R.string.something_went_wrong),
                                response.code().toString(),
                                ErrorCauses.NO_CONNECTION
                            )
                            showSearchHistory(false)
                        }
                    }

                    override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
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
        private const val SEARCH_USER_INPUT = "search_user_input"
        private val SEARCH_DEF: CharSequence = ""
        const val SEARCH_TRACKS_HISTORY = "search_track_history"
    }
}




