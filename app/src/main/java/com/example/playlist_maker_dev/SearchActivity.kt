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
import android.widget.ImageView
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

    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesService = retrofit.create(ITunesApi::class.java)
    private var inputValue: CharSequence = SEARCH_DEF
    private val tracks = mutableListOf<Track>()
    private val adapter = TrackAdapter(mutableListOf())

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        if (savedInstanceState != null) {
            inputValue = savedInstanceState.getCharSequence(SEARCH_USER_INPUT, SEARCH_DEF)
        }

        val backFromSettingsButton = findViewById<Button>(R.id.buttonBackFromSearch)
        backFromSettingsButton.setOnClickListener {
            finish()
        }

        val clearButton = findViewById<ImageView>(R.id.search_delete_button)
        clearButton.setOnClickListener {
            binding.inputEditText.setText(getString(R.string.emptyString))
            binding.inputEditText.onEditorAction(EditorInfo.IME_ACTION_DONE)
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(clearButton.windowToken, 0)
            binding.placeholderMessage.visibility = View.GONE
            binding.placeholderImage.visibility = View.GONE
            binding.buttonReload.visibility = View.GONE
            tracks.clear()
            adapter.notifyDataSetChanged()
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.isVisible = clearButtonVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {
                inputValue = s.toString()
            }
        }
        binding.inputEditText.addTextChangedListener(simpleTextWatcher)

        adapter.tracks = tracks
        binding.rvTracks.adapter = adapter

        binding.inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                findTrack()
            }
            true
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putCharSequence(SEARCH_USER_INPUT, inputValue)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        inputValue = savedInstanceState.getString(SEARCH_USER_INPUT).toString()
        binding.inputEditText.setText(inputValue)
    }

    private fun clearButtonVisibility(s: CharSequence?): Boolean = !s.isNullOrEmpty()

    private companion object {
        private const val SEARCH_USER_INPUT = "SEARCH_USER_INPUT"
        private val SEARCH_DEF: CharSequence = ""
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showMessage(text: String, additionalMessage: String, cause: ErrorCauses) {
        if (text.isNotEmpty()) {
            when (cause) {
                ErrorCauses.NO_RESULTS ->
                    binding.placeholderImage.setImageResource(R.drawable.vector_nothing_found)

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
                binding.placeholderImage.visibility = View.GONE
                binding.buttonReload.visibility = View.GONE
                binding.placeholderMessage.visibility = View.GONE
                findTrack()
            }
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(binding.inputEditText.windowToken, 0)

            tracks.clear()
            adapter.notifyDataSetChanged()
            binding.placeholderMessage.text = text
            if (additionalMessage.isNotEmpty()) {
                Toast.makeText(applicationContext, additionalMessage, Toast.LENGTH_LONG)
                    .show()
            }
        } else {
            binding.placeholderMessage.visibility = View.GONE
            binding.placeholderImage.visibility = View.GONE
        }
    }

    private fun findTrack() {
        if (binding.inputEditText.text.isNotEmpty()) {
            iTunesService.findTrack(binding.inputEditText.text.toString())
                .enqueue(object : Callback<TrackResponse> {
                    @SuppressLint("NotifyDataSetChanged")
                    override fun onResponse(
                        call: Call<TrackResponse>,
                        response: Response<TrackResponse>
                    ) {
                        if (response.code() == 200) {
                            tracks.clear()
                            if (!response.body()?.results.isNullOrEmpty()) {
                                tracks.addAll(response.body()?.results ?: emptyList())
                                adapter.notifyDataSetChanged()
                            }
                            if (tracks.isEmpty()) {
                                showMessage(
                                    getString(R.string.nothing_found),
                                    "",
                                    ErrorCauses.NO_RESULTS
                                )
                            } else {
                                showMessage("", "", ErrorCauses.EVERYTHING_GOOD)
                            }
                        } else {
                            showMessage(
                                getString(R.string.something_went_wrong),
                                response.code().toString(), ErrorCauses.NO_CONNECTION
                            )
                        }
                    }

                    override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                        showMessage(
                            getString(R.string.something_went_wrong),
                            t.message.toString(), ErrorCauses.NO_CONNECTION
                        )
                    }

                })
        }
    }

    enum class ErrorCauses {
        NO_CONNECTION, NO_RESULTS, EVERYTHING_GOOD
    }
}




