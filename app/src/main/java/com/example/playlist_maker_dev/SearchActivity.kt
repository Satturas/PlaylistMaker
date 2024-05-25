package com.example.playlist_maker_dev

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity



class SearchActivity : AppCompatActivity() {

    private var inputValue: CharSequence = SEARCH_DEF
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        if (savedInstanceState != null) {
            inputValue = savedInstanceState.getCharSequence(SEARCH_USER_INPUT, SEARCH_DEF)
        }

        val backFromSettingsButton = findViewById<Button>(R.id.buttonBackFromSearch)
        backFromSettingsButton.setOnClickListener {
            finish()
        }

        //val linearLayout = findViewById<LinearLayout>(R.id.search_field)
        val inputEditText = findViewById<EditText>(R.id.inputEditText)
        val clearButton = findViewById<ImageView>(R.id.search_delete_button)


        clearButton.setOnClickListener {
            inputEditText.setText("")
            inputEditText.onEditorAction(EditorInfo.IME_ACTION_DONE)
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {
                inputValue = s.toString()
            }
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putCharSequence(SEARCH_USER_INPUT, inputValue)
    }

    companion object {
        const val SEARCH_USER_INPUT = "SEARCH_USER_INPUT"
        val SEARCH_DEF: CharSequence = ""
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

}
