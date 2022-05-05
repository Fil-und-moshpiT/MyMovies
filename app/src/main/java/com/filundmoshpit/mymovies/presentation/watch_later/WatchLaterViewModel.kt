package com.filundmoshpit.mymovies.presentation.watch_later

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.filundmoshpit.mymovies.domain.MovieEntity
import com.filundmoshpit.mymovies.domain.usecase.LoadWatchLaterUseCase
import com.filundmoshpit.mymovies.presentation.LoadingStatuses
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class WatchLaterViewModel(private val useCase: LoadWatchLaterUseCase) : ViewModel() {

    private val _status = MutableStateFlow(LoadingStatuses.EMPTY)
    val status: StateFlow<LoadingStatuses> = _status

    private val _movies = MutableStateFlow(emptyList<MovieEntity>())
    val movies: StateFlow<List<MovieEntity>> = _movies

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _status.value = LoadingStatuses.LOADING

            useCase.load().collect {
                _movies.value = it
                _status.value = if (it.isEmpty()) LoadingStatuses.EMPTY else LoadingStatuses.LOADED
            }
        }
    }
}

class WatchLaterViewModelFactory @Inject constructor(private val useCase: LoadWatchLaterUseCase) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = WatchLaterViewModel(useCase) as T
}