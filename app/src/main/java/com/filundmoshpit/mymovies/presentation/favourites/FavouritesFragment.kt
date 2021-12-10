package com.filundmoshpit.mymovies.presentation.favourites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.filundmoshpit.mymovies.MainActivity
import com.filundmoshpit.mymovies.databinding.FragmentFavouritesBinding
import com.filundmoshpit.mymovies.domain.MovieEntity
import com.filundmoshpit.mymovies.BusEvents
import com.filundmoshpit.mymovies.presentation.LoadingStatuses
import kotlinx.coroutines.flow.collect
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class FavouritesFragment : Fragment() {

    private lateinit var viewModel: FavouritesViewModel

    private lateinit var binding: FragmentFavouritesBinding

    private var listAdapter = FavouritesListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        EventBus.getDefault().register(this)

        //Animation
//        exitTransition = Hold()
//        reenterTransition = Hold()

        viewModel =
            ViewModelProvider(requireActivity(), FavouritesViewModelFactory(MainActivity.favouritesUseCase))
                .get(FavouritesViewModel::class.java)

        //ViewModel observer on create
        lifecycleScope.launchWhenCreated { viewModel.movies.collect { listAdapter.submitList(it as MutableList<MovieEntity>) } }

        //Load data
        viewModel.load()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFavouritesBinding.inflate(inflater, container, false)

        binding.list.setHasFixedSize(true)
        binding.list.itemAnimator = null
        binding.list.adapter = listAdapter

        //ViewModel observer on start
        lifecycleScope.launchWhenStarted { viewModel.status.collect { onStatusChange(it) } }

        //Animation
        //Required for reenter transition
//        postponeEnterTransition()
//        binding.root.doOnPreDraw { startPostponedEnterTransition() }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()

        EventBus.getDefault().unregister(this)
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

    @Subscribe
    fun onFavouriteChanged(event: BusEvents.FavouriteChanged) {
//        Toast.makeText(context, "Favourite changed", Toast.LENGTH_SHORT).show()

        viewModel.load()
    }
}