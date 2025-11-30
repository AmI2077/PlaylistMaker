package com.example.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged

class SearchActivity : AppCompatActivity() {

    private var query: String = QUERY
    private lateinit var editText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val padding = resources.getDimensionPixelSize(R.dimen.padding_16)
            v.setPadding(padding, systemBars.top, padding, systemBars.bottom)
            insets
        }
        editText = findViewById<EditText>(R.id.search_bar)
        editText.setText(query)

        val clearSearchBtn = findViewById<ImageView>(R.id.clear_search_btn)
        clearSearchBtn.setOnClickListener { onClearBtnClick(editText) }

        setTextWatcher(editText)

        val backBtn = findViewById<ImageView>(R.id.back_btn)
        backBtn.setOnClickListener { onBackBtnClick() }

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        query = savedInstanceState.getString(USER_QUERY) ?: ""
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(USER_QUERY, query)
    }

    private fun onBackBtnClick() {
        finish()
    }

    private fun onClearBtnClick(editText: EditText) {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(editText.windowToken, 0)
        editText.setText("")
        query = ""
    }

    private fun setTextWatcher(editText: EditText) {
        val clearSearchBtn = findViewById<ImageView>(R.id.clear_search_btn)
        editText.addTextChangedListener(
            onTextChanged = { s, start, before, count ->
                clearSearchBtn.visibility = setClearBtnVisibility(s)
                query = s.toString()
            }
        )
    }

    private fun setClearBtnVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    companion object {
        private const val USER_QUERY = "USER_QUERY"
        private const val QUERY = ""
    }
}