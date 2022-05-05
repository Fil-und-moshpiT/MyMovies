package com.filundmoshpit.mymovies.presentation.movie_card

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.filundmoshpit.mymovies.domain.MovieEntity
import com.filundmoshpit.mymovies.domain.usecase.InsertMovieToInternalUseCase
import com.filundmoshpit.mymovies.domain.usecase.UpdateMovieStatusUseCase
import com.filundmoshpit.mymovies.presentation.LoadingStatuses
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class MovieCardViewModel(
    private val updateMovieUseCase: UpdateMovieStatusUseCase,
    private val insertMovieToInternalUseCase: InsertMovieToInternalUseCase
) : ViewModel() {

    private val _status = MutableStateFlow(LoadingStatuses.EMPTY)
    val status: StateFlow<LoadingStatuses> = _status
    private val _movie = MutableStateFlow<MovieEntity?>(null)
    val movie: StateFlow<MovieEntity?> = _movie

    fun setStatus(value: LoadingStatuses) {
        _status.value = value
    }

    fun load(movie: MovieEntity) {
        setStatus(LoadingStatuses.LOADING)

        viewModelScope.launch(Dispatchers.IO) {
            launch {
                insertMovieToInternalUseCase.insert(movie)
            }.join()

            _movie.value = movie

            setStatus(LoadingStatuses.LOADED)
        }
    }

    fun changeWatchLater() = movie.value?.changeWatchLater()

    fun changeFavourite() = movie.value?.changeFavourite()

    fun updateFavourite() {
        val current = movie.value

        if (current != null) {
            viewModelScope.launch(Dispatchers.IO) {
                updateMovieUseCase.updateFavourite(current)
            }
        }
    }

    fun updateWatchLater() {
        val current = movie.value

        if (current != null) {
            viewModelScope.launch(Dispatchers.IO) {
                updateMovieUseCase.updateWatchLater(current)
            }
        }
    }
}

class MovieCardViewModelFactory @Inject constructor(
    private val updateMovieUseCase: UpdateMovieStatusUseCase,
    private val insertMovieToInternalUseCase: InsertMovieToInternalUseCase
) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = MovieCardViewModel(
        updateMovieUseCase,
        insertMovieToInternalUseCase
    ) as T
}