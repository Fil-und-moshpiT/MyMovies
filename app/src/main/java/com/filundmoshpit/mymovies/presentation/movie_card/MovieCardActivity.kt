package com.filundmoshpit.mymovies.presentation.movie_card

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.filundmoshpit.mymovies.R
import com.filundmoshpit.mymovies.databinding.ActivityMovieCardBinding
import com.filundmoshpit.mymovies.domain.MovieEntity
import com.filundmoshpit.mymovies.presentation.LoadingStatuses
import com.filundmoshpit.mymovies.presentation.contextComponent
import kotlinx.coroutines.flow.collect
import javax.inject.Inject
import kotlin.math.floor

class MovieCardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMovieCardBinding

    @Inject
    lateinit var viewModelFactory: MovieCardViewModelFactory
    private lateinit var viewModel: MovieCardViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //DAGGER
        contextComponent.inject(this)

        binding = ActivityMovieCardBinding.inflate(layoutInflater)

        setContentView(binding.root)

        viewModel = ViewModelProvider(this, viewModelFactory).get(MovieCardViewModel::class.java)

        binding.watchLaterButton.setOnClickListener {
            viewModel.changeWatchLater()
            viewModel.updateWatchLater()

            val current = viewModel.movie.value
            if (current != null) {
                updateButtonIcons(current.watchLater, current.favourite)
            }
        }

        binding.favouriteButton.setOnClickListener {
            viewModel.changeFavourite()
            viewModel.updateFavourite()

            val current = viewModel.movie.value
            if (current != null) {
                updateButtonIcons(current.watchLater, current.favourite)
            }
        }

        //Collecting view model changes
        lifecycleScope.launchWhenCreated { viewModel.status.collect { onStatusChange(it) } }
        lifecycleScope.launchWhenCreated { viewModel.movie.collect { onMovieChanged(it) } }

        //Get data
        val movie = intent.getSerializableExtra("movie") as MovieEntity?
        if (movie == null) {
            viewModel.setStatus(LoadingStatuses.LOADED)
        } else {
            viewModel.load(movie)
        }
    }

    private fun onStatusChange(status: LoadingStatuses) {  }

    private fun onMovieChanged(movie: MovieEntity?) {
        if (movie == null) {
            return
        }

        binding.movieTitle.text = movie.name
        binding.movieDescription.text = movie.description

        Glide.with(this)
            .load(movie.imageBig)
            .into(binding.moviePoster)

        setRatingIcons(movie.rating)

        updateButtonIcons(movie.watchLater, movie.favourite)
    }

    private fun setRatingIcons(movieRating: Float) {
        var rating = (floor(movieRating) / 2.0).toFloat()

        setRatingIcon(binding.movieTmdbRatingStar1, rating)
        rating--
        setRatingIcon(binding.movieTmdbRatingStar2, rating)
        rating--
        setRatingIcon(binding.movieTmdbRatingStar3, rating)
        rating--
        setRatingIcon(binding.movieTmdbRatingStar4, rating)
        rating--
        setRatingIcon(binding.movieTmdbRatingStar5, rating)
    }

    private fun setRatingIcon(view: ImageView?, rating: Float) {
        if (view == null) {
            return
        }

        when {
            rating >= 1 ->
                view.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_rating_full))
            rating >= 0.5 ->
                view.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_rating_half))
            else ->
                view.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_rating_empty))
        }
    }

    private fun updateButtonIcons(isWatchLater: Boolean, isFavourite: Boolean) {
        var iconWatchLater =
            ContextCompat.getDrawable(this, R.drawable.ic_list_item_watch_later_unchecked)

        var iconFavourite =
            ContextCompat.getDrawable(this, R.drawable.ic_list_item_favourite_unchecked)

        if (isWatchLater) {
            iconWatchLater =
                ContextCompat.getDrawable(this, R.drawable.ic_list_item_watch_later_checked)
        }

        if (isFavourite) {
            iconFavourite =
                AppCompatResources.getDrawable(this, R.drawable.ic_list_item_favourite_checked)
        }

        binding.watchLaterButton.setCompoundDrawablesWithIntrinsicBounds(
            iconWatchLater,
            null,
            null,
            null
        )

        binding.favouriteButton.setCompoundDrawablesWithIntrinsicBounds(
            iconFavourite,
            null,
            null,
            null
        )
    }
}