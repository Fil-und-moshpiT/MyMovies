package com.filundmoshpit.mymovies.presentation.movie_card

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.filundmoshpit.mymovies.MainActivity
import com.filundmoshpit.mymovies.R
import com.filundmoshpit.mymovies.domain.MovieEntity
import com.filundmoshpit.mymovies.presentation.util.LoadingStatuses
import kotlinx.coroutines.flow.collect

class MovieCardFragment : Fragment() {

    private lateinit var movie: MovieEntity

    private lateinit var viewModel: MovieCardViewModel

    private lateinit var viewMovieCardPoster: ImageView
    private lateinit var viewMovieCardName: TextView
    private lateinit var viewMovieCardDescription: TextView
    private lateinit var viewMovieCardLoadingSpinner: ProgressBar

    private lateinit var viewMovieCardWatchLaterButton: Button
    private lateinit var viewMovieCardFavouriteButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel =
            ViewModelProvider(requireActivity(), MovieCardViewModelFactory(MainActivity.movieCardUseCase))
                .get(MovieCardViewModel::class.java)

        //Collecting view model changes
        lifecycleScope.launchWhenStarted {
            viewModel.status.collect { onStatusChange(it) }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.movie.collect { onDataLoaded(it) }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_movie_card, container, false)

        viewMovieCardPoster = root.findViewById(R.id.movie_poster)
        viewMovieCardName = root.findViewById(R.id.movie_name)
        viewMovieCardDescription = root.findViewById(R.id.movie_description)
        viewMovieCardLoadingSpinner = root.findViewById(R.id.movie_card_loading_spinner)

        viewMovieCardWatchLaterButton = root.findViewById(R.id.movie_card_watch_later_button)
        viewMovieCardWatchLaterButton.setOnClickListener {
            movie.changeWatchLater()
            viewModel.updateWatchLater(movie)
            updateIcons()
        }

        viewMovieCardFavouriteButton = root.findViewById(R.id.movie_card_favourite_button)
        viewMovieCardFavouriteButton.setOnClickListener {
            movie.changeFavourite()
            viewModel.updateFavourite(movie)
            updateIcons()
        }

        return root
    }

    override fun onStart() {
        super.onStart()

        //Loading data
        val id = arguments?.getInt("id")
        viewModel.load(id ?: -1)
    }

    private fun onStatusChange(status: LoadingStatuses) {
        when (status) {
            LoadingStatuses.EMPTY -> {
                viewMovieCardPoster.visibility = View.GONE
                viewMovieCardName.visibility = View.GONE
                viewMovieCardDescription.visibility = View.GONE
                viewMovieCardLoadingSpinner.visibility = View.GONE
            }

            LoadingStatuses.LOADING -> {
                viewMovieCardPoster.visibility = View.GONE
                viewMovieCardName.visibility = View.GONE
                viewMovieCardDescription.visibility = View.GONE
                viewMovieCardLoadingSpinner.visibility = View.VISIBLE
            }

            LoadingStatuses.LOADED -> {
                viewMovieCardPoster.visibility = View.VISIBLE
                viewMovieCardName.visibility = View.VISIBLE
                viewMovieCardDescription.visibility = View.VISIBLE
                viewMovieCardLoadingSpinner.visibility = View.GONE
            }
        }
    }

    private fun onDataLoaded(movie: MovieEntity) {
        this.movie = movie

        viewMovieCardName.text = movie.getName()
        viewMovieCardDescription.text = movie.getDescription()

        updateIcons()

        Glide.with(this)
            .load(movie.getImage())
            .into(viewMovieCardPoster)
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

        viewMovieCardWatchLaterButton.setCompoundDrawablesWithIntrinsicBounds(iconWatchLater, null, null, null)
        viewMovieCardFavouriteButton.setCompoundDrawablesWithIntrinsicBounds(iconFavourite, null, null, null)
    }
}