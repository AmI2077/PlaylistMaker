package com.example.playlistmaker

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.models.Track
import com.example.playlistmaker.searchRecycler.SearchAdapter

class SearchActivity : AppCompatActivity() {

    private var query: String = QUERY
    private lateinit var editText: EditText

    private val tracks = getTracks()
    private val adapter = SearchAdapter(tracks)

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

        setupRecyclerAdapter()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        query = savedInstanceState.getString(USER_QUERY) ?: ""
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(USER_QUERY, query)
    }

    private fun setupRecyclerAdapter() {
        val recyclerView = findViewById<RecyclerView>(R.id.tracks_recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun getTracks(): MutableList<Track> {
        return mutableListOf(
            Track(
                "Smells Like Teen Spirit",
                "Nirvana",
                "5:01",
                "https://is5-ssl.mzstatic.com/image/thumb/Music115/v4/7b/58/c2/7b58c21a-2b51-2bb2-e59a-9bb9b96ad8c3/00602567924166.rgb.jpg/100x100bb.jpg"
            ),
            Track(
                "Billie Jean",
                "Michael Jackson",
                "4:35",
                "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/3d/9d/38/3d9d3811-71f0-3a0e-1ada-3004e56ff852/827969428726.jpg/100x100bb.jpg"
            ),
            Track(
                "Stayin' Alive",
                "Bee Gees",
                "4:10",
                "https://is4-ssl.mzstatic.com/image/thumb/Music115/v4/1f/80/1f/1f801fc1-8c0f-ea3e-d3e5-387c6619619e/16UMGIM86640.rgb.jpg/100x100bb.jpg"
            ),
            Track(
                "Whole Lotta Love",
                "Led Zeppelin",
                "5:33",
                "https://is2-ssl.mzstatic.com/image/thumb/Music62/v4/7e/17/e3/7e17e33f-2efa-2a36-e916-7f808576cf6b/mzm.fyigqcbs.jpg/100x100bb.jpg"
            ),
            Track(
                "Sweet Child O'Mine",
                "Guns N' Roses",
                "5:03",
                "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/a0/4d/c4/a04dc484-03cc-02aa-fa82-5334fcb4bc16/18UMGIM24878.rgb.jpg/100x100bb.jpg "
            ),
        )
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