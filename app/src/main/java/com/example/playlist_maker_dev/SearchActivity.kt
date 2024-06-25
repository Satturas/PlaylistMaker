package com.example.playlist_maker_dev

import android.content.Context
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Im
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private val iTunesBaseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesService = retrofit.create(ITunesApi::class.java)

    private lateinit var searchButton: Button
    private lateinit var queryInput: EditText
    private lateinit var placeholderMessage: TextView
    private lateinit var trackList: RecyclerView

    private var inputValue: CharSequence = SEARCH_DEF

    private val tracks = ArrayList<Track>()
    private val adapter = TrackAdapter(mutableListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        placeholderMessage = findViewById(R.id.placeholderMessage)
        queryInput = findViewById(R.id.inputEditText)
        trackList = findViewById(R.id.rvTracks)

        if (savedInstanceState != null) {
            inputValue = savedInstanceState.getCharSequence(SEARCH_USER_INPUT, SEARCH_DEF)
        }

        val backFromSettingsButton = findViewById<Button>(R.id.buttonBackFromSearch)
        backFromSettingsButton.setOnClickListener {
            finish()
        }

        val clearButton = findViewById<ImageView>(R.id.search_delete_button)
        clearButton.setOnClickListener {
            queryInput.setText(getString(R.string.emptyString))
            queryInput.onEditorAction(EditorInfo.IME_ACTION_DONE)
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(clearButton.getWindowToken(), 0)
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
        queryInput.addTextChangedListener(simpleTextWatcher)

        adapter.tracks = tracks
        trackList.adapter = adapter

        queryInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (queryInput.text.isNotEmpty()) {
                    iTunesService.findTrack(queryInput.text.toString())
                        .enqueue(object : Callback<TrackResponse> {
                            override fun onResponse(
                                call: Call<TrackResponse>,
                                response: Response<TrackResponse>
                            ) {
                                if (response.code() == 200) {
                                    tracks.clear()
                                    if (response.body()?.results?.isNotEmpty() == true) {
                                        tracks.addAll(response.body()?.results!!)
                                        adapter.notifyDataSetChanged()
                                    }
                                    if (tracks.isEmpty()) {
                                        showMessage(getString(R.string.nothing_found), "")
                                    } else {
                                        showMessage("", "")
                                    }
                                } else {
                                    showMessage(
                                        getString(R.string.something_went_wrong),
                                        response.code().toString()
                                    )
                                }
                            }

                            override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                                showMessage(
                                    getString(R.string.something_went_wrong),
                                    t.message.toString()
                                )
                            }

                        })
                }
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
        queryInput.setText(inputValue)
    }

    private fun clearButtonVisibility(s: CharSequence?): Boolean = !s.isNullOrEmpty()

    private companion object {
        private const val SEARCH_USER_INPUT = "SEARCH_USER_INPUT"
        private val SEARCH_DEF: CharSequence = ""
    }

    private fun showMessage(text: String, additionalMessage: String) {
        if (text.isNotEmpty()) {
            placeholderMessage.visibility = View.VISIBLE
            tracks.clear()
            adapter.notifyDataSetChanged()
            placeholderMessage.text = text
            if (additionalMessage.isNotEmpty()) {
                Toast.makeText(applicationContext, additionalMessage, Toast.LENGTH_LONG)
                    .show()
            }
        } else {
            placeholderMessage.visibility = View.GONE
        }
    }
}



