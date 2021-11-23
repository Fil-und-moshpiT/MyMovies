package com.filundmoshpit.mymovies.presentation.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.filundmoshpit.mymovies.MainActivity
import com.filundmoshpit.mymovies.R
import com.filundmoshpit.mymovies.domain.MovieEntity
import com.filundmoshpit.mymovies.presentation.util.LoadingStatuses
import kotlinx.coroutines.flow.collect

class SearchFragment : Fragment() {

    private lateinit var viewModel: SearchViewModel

    private lateinit var viewSearchText: EditText
    private lateinit var viewSearchButton: Button
    private lateinit var viewSearchList: RecyclerView
    private lateinit var viewSearchErrorLabel: TextView
    private lateinit var viewSearchLoadingSpinner: ProgressBar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_search, container, false)

        viewModel =
            ViewModelProvider(requireActivity(), SearchViewModelFactory(MainActivity.searchUseCase))
                .get(SearchViewModel::class.java)

        val moviesAdapter = SearchListAdapter(viewModel)

        //Bindings
        viewSearchText = root.findViewById(R.id.et_search)
        viewSearchText.addTextChangedListener(LocalTextWatcher())

        viewSearchButton = root.findViewById(R.id.b_search)
        viewSearchButton.setOnClickListener { viewModel.search() }

        viewSearchList = root.findViewById(R.id.rv_search_list)
        viewSearchList.adapter = moviesAdapter

        viewSearchErrorLabel = root.findViewById(R.id.tv_search_error_label)

        viewSearchLoadingSpinner = root.findViewById(R.id.pb_search_loading_spinner)

        //ViewModel observers
        lifecycleScope.launchWhenStarted { viewModel.movies.collect { moviesAdapter.submitList(it as MutableList<MovieEntity>) } }
        lifecycleScope.launchWhenStarted { viewModel.status.collect { onStatusChange(it) } }
        lifecycleScope.launchWhenStarted { viewModel.errorTextId.collect { onErrorChange(it) } }

        initialize()

        return root
    }

    private fun initialize() {
        viewSearchText.setText(viewModel.getQuery())
    }

    private fun onStatusChange(status: LoadingStatuses) {
        when (status) {
            LoadingStatuses.EMPTY -> {
                viewSearchList.visibility = View.GONE
                viewSearchErrorLabel.visibility = View.VISIBLE
                viewSearchLoadingSpinner.visibility = View.GONE
            }

            LoadingStatuses.LOADED -> {
                viewSearchList.visibility = View.VISIBLE
                viewSearchErrorLabel.visibility = View.GONE
                viewSearchLoadingSpinner.visibility = View.GONE
            }

            LoadingStatuses.LOADING -> {
                viewSearchList.visibility = View.GONE
                viewSearchErrorLabel.visibility = View.GONE
                viewSearchLoadingSpinner.visibility = View.VISIBLE
            }
        }
    }

    private fun onErrorChange(errorStringId: Int) {
        viewSearchErrorLabel.text = resources.getText(errorStringId)
    }

    inner class LocalTextWatcher : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            viewModel.setQuery(s.toString())
        }
    }
}