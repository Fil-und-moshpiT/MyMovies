package com.filundmoshpit.mymovies.presentation.watch_later

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
import com.filundmoshpit.mymovies.presentation.util.ListLoadingStatus
import kotlinx.coroutines.flow.collect

class WatchLaterFragment : Fragment() {

    private lateinit var viewModel: WatchLaterViewModel

    private lateinit var viewWatchLaterList: RecyclerView
    private lateinit var viewWatchLaterEmptyListLabel: TextView
    private lateinit var viewWatchLaterLoadingSpinner: ProgressBar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_watch_later, container, false)

        viewModel =
            ViewModelProvider(requireActivity(), WatchLaterViewModelFactory(MainActivity.watchLaterUseCase))
                .get(WatchLaterViewModel::class.java)

        val moviesAdapter = WatchLaterListAdapter(viewModel)

        viewWatchLaterList = root.findViewById(R.id.rv_watch_later_list)
        viewWatchLaterList.adapter = moviesAdapter

        viewWatchLaterEmptyListLabel = root.findViewById(R.id.tv_watch_later_empty_list_label)

        viewWatchLaterLoadingSpinner = root.findViewById(R.id.pb_watch_later_loading_spinner)

        //ViewModel observers
        viewModel.movies.observe(viewLifecycleOwner, { moviesAdapter.submitList(it as MutableList<MovieEntity>) })
        //viewModel.status.observe(viewLifecycleOwner, { onStatusChange(it) })

        lifecycleScope.launchWhenStarted { viewModel.status.collect { onStatusChange(it) } }

        viewModel.load()

        return root
    }

    private fun onStatusChange(status: ListLoadingStatus) {
        when (status) {
            ListLoadingStatus.EMPTY -> {
                viewWatchLaterList.visibility = View.GONE
                viewWatchLaterEmptyListLabel.visibility = View.VISIBLE
                viewWatchLaterLoadingSpinner.visibility = View.GONE
            }

            ListLoadingStatus.LOADED -> {
                viewWatchLaterList.visibility = View.VISIBLE
                viewWatchLaterEmptyListLabel.visibility = View.GONE
                viewWatchLaterLoadingSpinner.visibility = View.GONE
            }

            ListLoadingStatus.LOADING -> {
                viewWatchLaterList.visibility = View.GONE
                viewWatchLaterEmptyListLabel.visibility = View.GONE
                viewWatchLaterLoadingSpinner.visibility = View.VISIBLE
            }
        }
    }
}