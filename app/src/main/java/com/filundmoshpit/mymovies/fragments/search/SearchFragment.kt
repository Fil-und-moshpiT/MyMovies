package com.filundmoshpit.mymovies.fragments.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.filundmoshpit.mymovies.R
import com.filundmoshpit.mymovies.data.Movie

class SearchFragment : Fragment() {

    private lateinit var viewModel: SearchViewModel

    private lateinit var viewSearchText: EditText
    private lateinit var viewSearchButton: Button
    private lateinit var viewSearchList: RecyclerView
    private lateinit var viewSearchEmptyList: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_search, container, false)
        val moviesAdapter = MoviesAdapter()

        viewModel = ViewModelProvider(requireActivity()).get(SearchViewModel::class.java)

        viewSearchText = root.findViewById(R.id.et_search)
        viewSearchText.addTextChangedListener(LocalTextWatcher())

        viewSearchButton = root.findViewById(R.id.b_search)
        viewSearchButton.setOnClickListener { viewModel.search() }

        viewSearchList = root.findViewById(R.id.rv_search_list)
        viewSearchList.adapter = moviesAdapter

        viewSearchEmptyList = root.findViewById(R.id.tv_search_empty_list)

        //ViewModel observers
        viewModel.movies.observe(viewLifecycleOwner, {
            moviesAdapter.submitList(it as MutableList<Movie>)

            val isListEmpty = (it.size == 0)
            viewSearchList.visibility      = if (isListEmpty) View.GONE else View.VISIBLE
            viewSearchEmptyList.visibility = if (isListEmpty) View.VISIBLE else View.GONE
        })

        viewModel.searching.observe(viewLifecycleOwner, {
//            viewSearchList.isClickable = !it
            viewSearchButton.isClickable = !it
        })

        return root
    }

    inner class LocalTextWatcher : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            viewModel.setQuery(s.toString())
        }
    }
}