package com.filundmoshpit.mymovies.presentation.search

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.filundmoshpit.mymovies.databinding.FragmentSearchBinding
import com.filundmoshpit.mymovies.domain.MovieEntity
import com.filundmoshpit.mymovies.presentation.LoadingStatuses
import com.filundmoshpit.mymovies.presentation.contextComponent
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class SearchFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: SearchViewModelFactory

    private lateinit var viewModel: SearchViewModel

    private var _binding: FragmentSearchBinding? = null
    private val binding: FragmentSearchBinding
        get() = _binding!!

    override fun onAttach(context: Context) {
        context.contextComponent.inject(this)

        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(
            requireActivity(),
            viewModelFactory
        ).get(SearchViewModel::class.java)

        val listAdapter = SearchListAdapter()

        //Bindings
        binding.searchQuery.addTextChangedListener(LocalTextWatcher())

        binding.searchButton.setOnClickListener { viewModel.search() }

        binding.list.setHasFixedSize(true)
        binding.list.itemAnimator = null
        binding.list.adapter = listAdapter

        //ViewModel observers
        lifecycleScope.launchWhenStarted { viewModel.movies.collect { listAdapter.submitList(it as MutableList<MovieEntity>) } }
        lifecycleScope.launchWhenStarted { viewModel.status.collect { onStatusChange(it) } }
        lifecycleScope.launchWhenStarted { viewModel.errorTextId.collect { onErrorChange(it) } }

        initialize()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    private fun initialize() {
        binding.searchQuery.setText(viewModel.getQuery())
    }

    private fun onStatusChange(status: LoadingStatuses) {
        binding.errorLabel.visibility = View.GONE
        binding.loadingSpinner.visibility = View.GONE
        binding.list.visibility = View.GONE

        when (status) {
            LoadingStatuses.EMPTY -> {
                binding.errorLabel.visibility = View.VISIBLE
            }
            LoadingStatuses.LOADING -> {
                binding.loadingSpinner.visibility = View.VISIBLE
            }
            LoadingStatuses.LOADED -> {
                binding.list.visibility = View.VISIBLE
            }
        }
    }

    private fun onErrorChange(errorStringId: Int) {
        binding.errorLabel.text = resources.getText(errorStringId)
    }

    inner class LocalTextWatcher : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            viewModel.setQuery(s.toString())
        }
    }
}