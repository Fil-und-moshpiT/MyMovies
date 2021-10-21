package com.filundmoshpit.mymovies.presentation.favourites

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.filundmoshpit.mymovies.domain.FavouritesUseCase
import com.filundmoshpit.mymovies.domain.Movie
import com.filundmoshpit.mymovies.domain.SearchUseCase
import com.filundmoshpit.mymovies.presentation.util.ListLoadingStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FavouritesViewModel(private val useCase: FavouritesUseCase) : ViewModel() {

    val status = MutableLiveData(ListLoadingStatus.EMPTY)

    val movies = MutableLiveData<ArrayList<Movie>>()

    private fun setStatus(value: ListLoadingStatus) {
        status.postValue(value)
    }

    private fun replaceMovies(list: List<Movie>) {
        movies.postValue(list as ArrayList<Movie>)
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
}