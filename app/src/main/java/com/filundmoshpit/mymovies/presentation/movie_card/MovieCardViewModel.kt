package com.filundmoshpit.mymovies.presentation.movie_card

import androidx.lifecycle.ViewModel
import com.filundmoshpit.mymovies.domain.MovieCardUseCase
import com.filundmoshpit.mymovies.domain.MovieEntity
import com.filundmoshpit.mymovies.presentation.util.LoadingStatuses
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MovieCardViewModel(private val useCase: MovieCardUseCase) : ViewModel() {

    val status = MutableStateFlow(LoadingStatuses.EMPTY)
    val movie = MutableSharedFlow<MovieEntity>(1)

    private fun setStatus(value: LoadingStatuses) {
        status.value = value
    }

    fun load(id: Int) {
        setStatus(LoadingStatuses.LOADING)

        GlobalScope.launch(Dispatchers.IO) {
            val loaded = useCase.getMovieByID(id)

            movie.emit(loaded)

            setStatus(LoadingStatuses.LOADED)
        }
    }

    fun updateFavourite(movie: MovieEntity) {
        GlobalScope.launch(Dispatchers.IO) {
            useCase.updateFavourite(movie)
        }
    }

    fun updateWatchLater(movie: MovieEntity) {
        GlobalScope.launch(Dispatchers.IO) {
            useCase.updateWatchLater(movie)
        }
    }
}