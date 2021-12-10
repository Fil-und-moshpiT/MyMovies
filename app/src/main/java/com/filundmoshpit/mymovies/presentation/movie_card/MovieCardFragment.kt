package com.filundmoshpit.mymovies.presentation.movie_card

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.filundmoshpit.mymovies.MainActivity
import com.filundmoshpit.mymovies.R
import com.filundmoshpit.mymovies.databinding.FragmentMovieCardBinding
import com.filundmoshpit.mymovies.domain.MovieEntity
import com.filundmoshpit.mymovies.BusEvents
import com.filundmoshpit.mymovies.presentation.LoadingStatuses
import kotlinx.coroutines.flow.collect
import org.greenrobot.eventbus.EventBus




class MovieCardFragment : Fragment() {

    private lateinit var movie: MovieEntity

    private lateinit var viewModel: MovieCardViewModel

    private lateinit var binding: FragmentMovieCardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel =
            ViewModelProvider(requireActivity(), MovieCardViewModelFactory(MainActivity.movieCardUseCase))
                .get(MovieCardViewModel::class.java)

        //Animation
//        sharedElementEnterTransition = MaterialContainerTransform().apply {
//            drawingViewId = R.id.navigation_host_fragment
//            duration = resources.getInteger(R.integer.material_motion_duration_long_1).toLong()
//            scrimColor = Color.TRANSPARENT
//            setAllContainerColors(context!!.getColor(R.color.white))
//        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMovieCardBinding.inflate(inflater, container, false)

        binding.watchLaterButton.setOnClickListener {
            movie.changeWatchLater()
            viewModel.updateWatchLater(movie)

            EventBus.getDefault().post(BusEvents.WatchLaterChanged)

            updateIcons()
        }

        binding.favouriteButton.setOnClickListener {
            movie.changeFavourite()
            viewModel.updateFavourite(movie)

            EventBus.getDefault().post(BusEvents.FavouriteChanged)

            updateIcons()
        }

        //Collecting view model changes
        lifecycleScope.launchWhenCreated { viewModel.status.collect { onStatusChange(it) } }
        lifecycleScope.launchWhenCreated { viewModel.movie.collect { onDataLoaded(it) } }

        //Get data
        val id = arguments?.getInt("id")
        if (id == null) {
            viewModel.setStatus(LoadingStatuses.LOADED)
        }
        else {
            viewModel.load(id)
        }

        return binding.root
    }

    private fun onStatusChange(status: LoadingStatuses) {
        //TODO: Add error label
        binding.movieCardData.visibility = View.GONE

        when (status) {
            LoadingStatuses.EMPTY -> { binding.movieCardData.visibility = View.GONE }
            LoadingStatuses.LOADING -> { binding.movieCardData.visibility = View.GONE }
            LoadingStatuses.LOADED -> { binding.movieCardData.visibility = View.VISIBLE }
        }
    }

    private fun onDataLoaded(movie: MovieEntity) {
        this.movie = movie

        binding.movieTitle.text = movie.getName()
        binding.movieDescription.text = movie.getDescription()

        Glide.with(this)
            .load(movie.getImage())
            .into(binding.moviePoster)

        updateIcons()
    }

    private fun updateIcons() {
        val localContext = requireContext()

        var iconWatchLater = AppCompatResources.getDrawable(localContext, R.drawable.ic_list_item_watch_later_unchecked)
        var iconFavourite = AppCompatResources.getDrawable(localContext, R.drawable.ic_list_item_favourite_unchecked)

        if (movie.getWatchLater()) {
            iconWatchLater = AppCompatResources.getDrawable(localContext, R.drawable.ic_list_item_watch_later_checked)
        }

        if (movie.getFavourite()) {
            iconFavourite = AppCompatResources.getDrawable(localContext, R.drawable.ic_list_item_favourite_checked)
        }

        binding.watchLaterButton.setCompoundDrawablesWithIntrinsicBounds(iconWatchLater, null, null, null)
        binding.favouriteButton.setCompoundDrawablesWithIntrinsicBounds(iconFavourite, null, null, null)
    }
}