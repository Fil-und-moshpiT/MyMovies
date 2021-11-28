package com.filundmoshpit.mymovies.presentation.watch_later

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.filundmoshpit.mymovies.MainActivity
import com.filundmoshpit.mymovies.databinding.FragmentWatchLaterBinding
import com.filundmoshpit.mymovies.domain.MovieEntity
import com.filundmoshpit.mymovies.presentation.util.LoadingStatuses
import com.google.android.material.transition.Hold
import kotlinx.coroutines.flow.collect

class WatchLaterFragment : Fragment() {

    private lateinit var viewModel: WatchLaterViewModel

    private lateinit var binding: FragmentWatchLaterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Animation
        exitTransition = Hold()
        reenterTransition = Hold()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentWatchLaterBinding.inflate(inflater, container, false)

        viewModel =
            ViewModelProvider(requireActivity(), WatchLaterViewModelFactory(MainActivity.watchLaterUseCase))
                .get(WatchLaterViewModel::class.java)

        val listAdapter = WatchLaterListAdapter(viewModel)

        binding.list.setHasFixedSize(true)
        binding.list.itemAnimator = null
        binding.list.adapter = listAdapter

        //ViewModel observers
        lifecycleScope.launchWhenStarted { viewModel.movies.collect { listAdapter.submitList(it as MutableList<MovieEntity>) } }
        lifecycleScope.launchWhenStarted { viewModel.status.collect { onStatusChange(it) } }

        viewModel.load()

        //Animation
        //Required for reenter transition
        postponeEnterTransition()
        binding.root.doOnPreDraw { startPostponedEnterTransition() }

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