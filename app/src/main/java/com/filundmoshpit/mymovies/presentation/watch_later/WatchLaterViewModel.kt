package com.filundmoshpit.mymovies.presentation.watch_later

import androidx.lifecycle.ViewModel
import com.filundmoshpit.mymovies.domain.MovieEntity
import com.filundmoshpit.mymovies.domain.usecases.WatchLaterUseCase
import com.filundmoshpit.mymovies.presentation.LoadingStatuses
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class WatchLaterViewModel(private val useCase: WatchLaterUseCase) : ViewModel() {

    val status = MutableStateFlow(LoadingStatuses.EMPTY)

    val movies = MutableStateFlow(ArrayList<MovieEntity>())

    private fun setStatus(value: LoadingStatuses) {
        status.value = value
    }

    private fun replaceMovies(list: List<MovieEntity>) {
        movies.value = list as ArrayList<MovieEntity>
    }

    private fun clearMovies() {
        movies.value.clear()
    }

    fun load() {
        setStatus(LoadingStatuses.LOADING)
        clearMovies()

        GlobalScope.launch(Dispatchers.IO) {
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