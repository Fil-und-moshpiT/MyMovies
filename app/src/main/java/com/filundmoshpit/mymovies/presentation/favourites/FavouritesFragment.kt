package com.filundmoshpit.mymovies.presentation.favourites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.filundmoshpit.mymovies.MainActivity
import com.filundmoshpit.mymovies.R
import com.filundmoshpit.mymovies.databinding.FragmentFavouritesBinding
import com.filundmoshpit.mymovies.databinding.FragmentWatchLaterBinding
import com.filundmoshpit.mymovies.domain.MovieEntity
import com.filundmoshpit.mymovies.presentation.util.LoadingStatuses
import kotlinx.coroutines.flow.collect

class FavouritesFragment : Fragment() {

    private lateinit var viewModel: FavouritesViewModel

    private lateinit var binding: FragmentFavouritesBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFavouritesBinding.inflate(inflater, container, false)

        viewModel =
            ViewModelProvider(requireActivity(), FavouritesViewModelFactory(MainActivity.favouritesUseCase))
                .get(FavouritesViewModel::class.java)

        val listAdapter = FavouritesListAdapter()

        binding.list.itemAnimator = null
        binding.list.adapter = listAdapter

        //ViewModel observers
        lifecycleScope.launchWhenStarted { viewModel.movies.collect { listAdapter.submitList(it as MutableList<MovieEntity>) } }
        lifecycleScope.launchWhenStarted { viewModel.status.collect { onStatusChange(it) } }

        viewModel.load()

        return binding.root
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
}