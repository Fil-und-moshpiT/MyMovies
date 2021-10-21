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
import androidx.recyclerview.widget.RecyclerView
import com.filundmoshpit.mymovies.MainActivity
import com.filundmoshpit.mymovies.R
import com.filundmoshpit.mymovies.domain.Movie
import com.filundmoshpit.mymovies.presentation.util.ListLoadingStatus

class SearchFragment : Fragment() {

    private lateinit var viewModel: SearchViewModel

    private lateinit var viewSearchText: EditText
    private lateinit var viewSearchButton: Button
    private lateinit var viewSearchList: RecyclerView
    private lateinit var viewSearchEmptyListLabel: TextView
    private lateinit var viewSearchLoadingSpinner: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_search, container, false)

        //searchViewModel = ViewModelProvider(requireActivity()).get(SearchViewModel::class.java)
        viewModel =
            ViewModelProvider(requireActivity(), SearchViewModelFactory(MainActivity.searchUseCase))
                .get(SearchViewModel::class.java)

        val moviesAdapter = SearchListAdapter(viewModel)

        viewSearchText = root.findViewById(R.id.et_search)
        viewSearchText.addTextChangedListener(LocalTextWatcher())

        viewSearchButton = root.findViewById(R.id.b_search)
        viewSearchButton.setOnClickListener { viewModel.search() }

        viewSearchList = root.findViewById(R.id.rv_search_list)
        viewSearchList.adapter = moviesAdapter

        viewSearchEmptyListLabel = root.findViewById(R.id.tv_search_empty_list_label)

        viewSearchLoadingSpinner = root.findViewById(R.id.pb_search_loading_spinner)

        //ViewModel observers
        viewModel.movies.observe(viewLifecycleOwner, { moviesAdapter.submitList(it as MutableList<Movie>) })
        viewModel.status.observe(viewLifecycleOwner, { onStatusChange(it) })

        return root
    }

    private fun onStatusChange(status: ListLoadingStatus) {
        when (status) {
            ListLoadingStatus.EMPTY -> {
                viewSearchList.visibility = View.GONE
                viewSearchEmptyListLabel.visibility = View.VISIBLE
                viewSearchLoadingSpinner.visibility = View.GONE
            }

            ListLoadingStatus.LOADED -> {
                viewSearchList.visibility = View.VISIBLE
                viewSearchEmptyListLabel.visibility = View.GONE
                viewSearchLoadingSpinner.visibility = View.GONE
            }

            ListLoadingStatus.LOADING -> {
                viewSearchList.visibility = View.GONE
                viewSearchEmptyListLabel.visibility = View.GONE
                viewSearchLoadingSpinner.visibility = View.VISIBLE
            }
        }
    }

    inner class LocalTextWatcher : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            viewModel.setQuery(s.toString())
        }
    }
}