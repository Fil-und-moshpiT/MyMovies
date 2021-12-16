package com.filundmoshpit.mymovies.presentation.movie_card

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.filundmoshpit.mymovies.BusEvents
import com.filundmoshpit.mymovies.domain.MovieEntity
import com.filundmoshpit.mymovies.domain.usecases.MovieCardUseCase
import com.filundmoshpit.mymovies.presentation.LoadingStatuses
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus

class MovieCardViewModel(private val useCase: MovieCardUseCase) : ViewModel() {

    val status = MutableStateFlow(LoadingStatuses.EMPTY)
    val movie = MutableSharedFlow<MovieEntity>(1)

    fun setStatus(value: LoadingStatuses) {
        status.value = value
    }

    fun load(id: Int) {
        setStatus(LoadingStatuses.LOADING)

        viewModelScope.launch(Dispatchers.IO) {
            movie.emit(useCase.getMovieByID(id))

            setStatus(LoadingStatuses.LOADED)
        }
    }

    fun updateFavourite(movie: MovieEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            useCase.updateFavourite(movie)
            EventBus.getDefault().post(BusEvents.FavouriteChanged)
        }
    }

    fun updateWatchLater(movie: MovieEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            useCase.updateWatchLater(movie)
            EventBus.getDefault().post(BusEvents.WatchLaterChanged)
        }
    }
}

class MovieCardViewModelFactory(private val useCase: MovieCardUseCase) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MovieCardViewModel(useCase) as T
    }
}