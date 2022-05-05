package com.filundmoshpit.mymovies.presentation.favourites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.filundmoshpit.mymovies.domain.MovieEntity
import com.filundmoshpit.mymovies.domain.usecase.LoadFavouritesUseCase
import com.filundmoshpit.mymovies.presentation.LoadingStatuses
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavouritesViewModel(private val useCase: LoadFavouritesUseCase) : ViewModel() {

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

class FavouritesViewModelFactory @Inject constructor(private val useCase: LoadFavouritesUseCase) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = FavouritesViewModel(useCase) as T
}