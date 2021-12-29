package com.filundmoshpit.mymovies.presentation.movie_card

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.filundmoshpit.mymovies.BusEvents
import com.filundmoshpit.mymovies.domain.MovieEntity
import com.filundmoshpit.mymovies.domain.usecase.MovieCardUseCase
import com.filundmoshpit.mymovies.presentation.LoadingStatuses
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject

class MovieCardViewModel(private val useCase: MovieCardUseCase) : ViewModel() {

    private val _status = MutableStateFlow(LoadingStatuses.EMPTY)
    private val _movie = MutableStateFlow<MovieEntity?>(null)

    val status: StateFlow<LoadingStatuses>
        get() = _status.asStateFlow()
    val movie: StateFlow<MovieEntity?>
        get() = _movie.asStateFlow()

    fun setStatus(value: LoadingStatuses) {
        _status.value = value
    }

    fun load(id: Int) {
        setStatus(LoadingStatuses.LOADING)

        viewModelScope.launch(Dispatchers.IO) {
            _movie.value = useCase.getMovieByID(id)

            setStatus(LoadingStatuses.LOADED)
        }
    }

    fun changeWatchLater() = movie.value?.changeWatchLater()

    fun changeFavourite() = movie.value?.changeFavourite()

    fun updateFavourite() {
        val current = movie.value

        if (current != null) {
            viewModelScope.launch(Dispatchers.IO) {
                useCase.updateFavourite(current)
                EventBus.getDefault().post(BusEvents.FavouriteChanged)
            }
        }
    }

    fun updateWatchLater() {
        val current = movie.value

        if (current != null) {
            viewModelScope.launch(Dispatchers.IO) {
                useCase.updateWatchLater(current)
                EventBus.getDefault().post(BusEvents.WatchLaterChanged)
            }
        }
    }
}

class MovieCardViewModelFactory @Inject constructor(private val useCase: MovieCardUseCase) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MovieCardViewModel(useCase) as T
    }
}