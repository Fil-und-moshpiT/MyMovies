package com.filundmoshpit.mymovies.presentation.favourites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.filundmoshpit.mymovies.domain.MovieEntity
import com.filundmoshpit.mymovies.domain.usecase.FavouritesUseCase
import com.filundmoshpit.mymovies.presentation.LoadingStatuses
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavouritesViewModel(private val useCase: FavouritesUseCase) : ViewModel() {

    private val _status = MutableStateFlow(LoadingStatuses.EMPTY)
    private val _movies = MutableStateFlow(ArrayList<MovieEntity>())

    val status: StateFlow<LoadingStatuses>
        get() = _status.asStateFlow()

    val movies: StateFlow<ArrayList<MovieEntity>>
        get() = _movies.asStateFlow()

    private fun setStatus(value: LoadingStatuses) {
        _status.value = value
    }

    private fun replaceMovies(list: List<MovieEntity>) {
        _movies.value = list as ArrayList<MovieEntity>
    }

    private fun clearMovies() {
        movies.value.clear()
    }

    fun load() {
        setStatus(LoadingStatuses.LOADING)
        clearMovies()

        viewModelScope.launch(Dispatchers.IO) {
            val founded = useCase.load()

            if (founded.isNotEmpty()) {
                replaceMovies(founded)
                setStatus(LoadingStatuses.LOADED)
            }
            else {
                setStatus(LoadingStatuses.EMPTY)
            }
        }
    }
}

class FavouritesViewModelFactory @Inject constructor(private val useCase: FavouritesUseCase) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FavouritesViewModel(useCase) as T
    }
}