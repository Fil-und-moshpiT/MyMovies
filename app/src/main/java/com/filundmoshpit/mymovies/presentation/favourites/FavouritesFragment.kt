package com.filundmoshpit.mymovies.presentation.favourites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

class FavouritesFragment : Fragment() {

    private lateinit var viewModel: FavouritesViewModel

    private lateinit var viewFavouritesList: RecyclerView
    private lateinit var viewFavouritesEmptyListLabel: TextView
    private lateinit var viewFavouritesLoadingSpinner: ProgressBar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_favourites, container, false)
        val moviesAdapter = FavouritesListAdapter()

        //viewModel = ViewModelProvider(requireActivity()).get(FavouritesViewModel::class.java)
        viewModel =
            ViewModelProvider(requireActivity(), FavouritesViewModelFactory(MainActivity.favouritesUseCase))
                .get(FavouritesViewModel::class.java)

        viewFavouritesList = root.findViewById(R.id.rv_favourites_list)
        viewFavouritesList.adapter = moviesAdapter

        viewFavouritesEmptyListLabel = root.findViewById(R.id.tv_favourites_empty_list_label)

        viewFavouritesLoadingSpinner = root.findViewById(R.id.pb_favourites_loading_spinner)

        //ViewModel observers
        viewModel.movies.observe(viewLifecycleOwner, { moviesAdapter.submitList(it as MutableList<MovieEntity>) })
        //viewModel.status.observe(viewLifecycleOwner, { onStatusChange(it) })

        lifecycleScope.launchWhenStarted { viewModel.status.collect { onStatusChange(it) } }

        viewModel.load()

        return root
    }

    private fun onStatusChange(status: LoadingStatuses) {
        when (status) {
            LoadingStatuses.EMPTY -> {
                viewFavouritesList.visibility = View.GONE
                viewFavouritesEmptyListLabel.visibility = View.VISIBLE
                viewFavouritesLoadingSpinner.visibility = View.GONE
            }

            LoadingStatuses.LOADED -> {
                viewFavouritesList.visibility = View.VISIBLE
                viewFavouritesEmptyListLabel.visibility = View.GONE
                viewFavouritesLoadingSpinner.visibility = View.GONE
            }

            LoadingStatuses.LOADING -> {
                viewFavouritesList.visibility = View.GONE
                viewFavouritesEmptyListLabel.visibility = View.GONE
                viewFavouritesLoadingSpinner.visibility = View.VISIBLE
            }
        }
    }
}