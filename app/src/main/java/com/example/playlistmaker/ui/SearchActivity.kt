package com.example.playlistmaker.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.api.RetrofitClient
import com.example.playlistmaker.api.SearchResult
import com.example.playlistmaker.api.TracksRepository
import com.example.playlistmaker.models.Track
import com.example.playlistmaker.ui.searchRecycler.SearchRecyclerAdapter

class SearchActivity : AppCompatActivity() {

    private lateinit var editText: EditText
    private lateinit var clearSearchBtn: ImageView
    private lateinit var refreshBtn: Button
    private lateinit var tracksRecycler: RecyclerView
    private lateinit var emptyErrorView: LinearLayout
    private lateinit var networkErrorView: LinearLayout

    private var query = QUERY
    private var tracks = mutableListOf<Track>()
    private var searchAdapter = SearchRecyclerAdapter(tracks)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)

        editText = findViewById<EditText>(R.id.editText)
        clearSearchBtn = findViewById<ImageView>(R.id.clear_search_btn)
        refreshBtn = findViewById<Button>(R.id.refresh_btn)
        tracksRecycler = findViewById<RecyclerView>(R.id.tracks_recycler)
        emptyErrorView = findViewById<LinearLayout>(R.id.empty_result_view)
        networkErrorView = findViewById<LinearLayout>(R.id.network_error_view)

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

    private fun loadTracks(query: String) {
        TracksRepository(RetrofitClient.tracksApi) { result ->
            when (result) {
                is SearchResult.Success -> showTracks(result.tracks)
                is SearchResult.EmptyResult -> showEmptyResult()
                is SearchResult.Error -> showError(result.errorMessage)
                is SearchResult.NetworkError -> showNetworkError()
            }
        }.search(query.trim())
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showTracks(tracks: MutableList<Track>) {
        this.tracks.clear()
        this.tracks.addAll(tracks)
        searchAdapter.notifyDataSetChanged()
        setVisibility(
            View.VISIBLE,
            View.GONE,
            View.GONE
        )
    }

    private fun showError(code: String) {
        setVisibility(
            View.GONE,
            View.VISIBLE,
            View.GONE
        )
        Toast.makeText(this, "Ошибка: $code", Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showEmptyResult() {
        this.tracks.clear()
        searchAdapter.notifyDataSetChanged()
        setVisibility(
            View.GONE,
            View.VISIBLE,
            View.GONE
        )
    }

    private fun showNetworkError() {
        setVisibility(
            View.GONE,
            View.GONE,
            View.VISIBLE
        )
    }

    private fun setVisibility(
        recycler: Int,
        emptyView: Int,
        networkErrorView: Int,
    ) {
        tracksRecycler.visibility = recycler
        emptyErrorView.visibility = emptyView
        this.networkErrorView.visibility = networkErrorView
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
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        tracksRecycler.adapter = searchAdapter
    }

    private fun setupClickListeners() {
        clearSearchBtn.setOnClickListener { onClearSearchBtnClick() }
        refreshBtn.setOnClickListener { loadTracks(query) }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun onClearSearchBtnClick() {
        query = ""
        editText.setText("")
        this.tracks.clear()
        searchAdapter.notifyDataSetChanged()
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(currentFocus?.windowToken, 0)

    }

    private fun setTextWatcher() {
        editText.doOnTextChanged { s, p1, p2, p3 ->
            setClearBtnVisibility(s.toString())
            query = s.toString()
        }
        editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loadTracks(query)
                true
            }
            false
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