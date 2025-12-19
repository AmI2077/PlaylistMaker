package com.example.playlistmaker.ui

import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.savedstate.serialization.saved
import com.example.playlistmaker.R
import com.example.playlistmaker.models.Track
import com.example.playlistmaker.ui.searchRecycler.SearchRecyclerAdapter

class SearchActivity : AppCompatActivity() {

    private lateinit var editText: EditText
    private lateinit var clearSearchBtn: ImageView
    private lateinit var tracksRecycler: RecyclerView

    private var query = QUERY
    private val tracks = listOf<Track>()
    private var searchAdapter = SearchRecyclerAdapter(tracks)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)

        editText = findViewById<EditText>(R.id.editText)
        clearSearchBtn = findViewById<ImageView>(R.id.clear_search_btn)
        tracksRecycler = findViewById<RecyclerView>(R.id.tracks_recycler)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val horizontalPadding = resources.getDimensionPixelSize(R.dimen.horizontalScreenPadding)
            val verticalPadding = resources.getDimensionPixelSize(R.dimen.verticalScreenPadding)
            v.setPadding(
                horizontalPadding,
                systemBars.top + verticalPadding,
                horizontalPadding,
                systemBars.bottom
            )
            insets
        }

        setTextWatcher()
        setupClickListeners()
        setupSearchAdapter()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putString(USER_QUERY, query)
    }

    override fun onRestoreInstanceState(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?
    ) {
        super.onRestoreInstanceState(savedInstanceState, persistentState)
        query = savedInstanceState?.getString(USER_QUERY).toString()
        editText.setText(query)
    }

    private fun setupSearchAdapter() {
        tracksRecycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        tracksRecycler.adapter = searchAdapter
    }

    private fun setupClickListeners() {
        clearSearchBtn.setOnClickListener {
            query = ""
            editText.setText("")
        }
    }

    private fun setTextWatcher() {
        editText.doOnTextChanged { s, p1, p2, p3 ->
            setClearBtnVisibility(s.toString())
            query = s.toString()
        }
    }

    private fun setClearBtnVisibility(s: String) {
        if (s.isNotEmpty()) {
            clearSearchBtn.visibility = View.VISIBLE
        } else {
            clearSearchBtn.visibility = View.GONE
        }
    }

    companion object {
        private const val USER_QUERY = "USER_QUERY"
        private const val QUERY = ""
    }
}