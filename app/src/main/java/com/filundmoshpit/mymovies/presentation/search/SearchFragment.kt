package com.filundmoshpit.mymovies.presentation.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.filundmoshpit.mymovies.MainActivity
import com.filundmoshpit.mymovies.databinding.FragmentSearchBinding
import com.filundmoshpit.mymovies.domain.MovieEntity
import com.filundmoshpit.mymovies.presentation.util.LoadingStatuses
import com.google.android.material.transition.Hold
import kotlinx.coroutines.flow.collect

class SearchFragment : Fragment() {

    private lateinit var viewModel: SearchViewModel

    private lateinit var binding: FragmentSearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Animation
        exitTransition = Hold()
        reenterTransition = Hold()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        viewModel =
            ViewModelProvider(requireActivity(), SearchViewModelFactory(MainActivity.searchUseCase))
                .get(SearchViewModel::class.java)

        val listAdapter = SearchListAdapter(viewModel)

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

        //Animation
        //Required for reenter transition
        postponeEnterTransition()
        binding.root.doOnPreDraw { startPostponedEnterTransition() }

        return binding.root
    }

    private fun initialize() {
        binding.searchQuery.setText(viewModel.getQuery())
    }

    private fun onStatusChange(status: LoadingStatuses) {
        binding.errorLabel.visibility = View.GONE
        binding.loadingSpinner.visibility = View.GONE
        binding.list.visibility = View.GONE

        when (status) {
            LoadingStatuses.EMPTY -> { binding.errorLabel.visibility = View.VISIBLE }
            LoadingStatuses.LOADING -> { binding.loadingSpinner.visibility = View.VISIBLE }
            LoadingStatuses.LOADED -> { binding.list.visibility = View.VISIBLE }
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