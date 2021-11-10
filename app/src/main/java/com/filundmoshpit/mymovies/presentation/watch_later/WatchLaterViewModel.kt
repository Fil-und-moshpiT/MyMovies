package com.filundmoshpit.mymovies.presentation.watch_later

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.filundmoshpit.mymovies.domain.MovieEntity
import com.filundmoshpit.mymovies.domain.WatchLaterUseCase
import com.filundmoshpit.mymovies.presentation.util.ListLoadingStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class WatchLaterViewModel(private val useCase: WatchLaterUseCase) : ViewModel() {

    //val status = MutableLiveData(ListLoadingStatus.EMPTY)
    val status = MutableStateFlow(ListLoadingStatus.EMPTY)

    val movies = MutableLiveData<ArrayList<MovieEntity>>()

    private fun setStatus(value: ListLoadingStatus) {
        //status.postValue(value)
        status.value = value
    }

    private fun replaceMovies(list: List<MovieEntity>) {
        movies.postValue(list as ArrayList<MovieEntity>)
    }

    private fun clearMovies() {
        movies.postValue(ArrayList())
    }

    fun load() {
        setStatus(ListLoadingStatus.LOADING)
        clearMovies()

        GlobalScope.launch(Dispatchers.IO) {
            val founded = useCase.load()

            if (founded.isNotEmpty()) {
                replaceMovies(founded)
                setStatus(ListLoadingStatus.LOADED)
            }
            else {
                setStatus(ListLoadingStatus.EMPTY)
            }
        }
    }

    fun updateFavourite(movie: MovieEntity) {
        GlobalScope.launch(Dispatchers.IO) {
            useCase.updateFavourite(movie)
        }
    }
}