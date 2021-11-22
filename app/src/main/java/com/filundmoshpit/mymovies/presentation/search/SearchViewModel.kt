package com.filundmoshpit.mymovies.presentation.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.filundmoshpit.mymovies.domain.MovieEntity
import com.filundmoshpit.mymovies.domain.SearchUseCase
import com.filundmoshpit.mymovies.presentation.util.LoadingStatuses
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(private val useCase: SearchUseCase) : ViewModel() {

    //val status = MutableLiveData(ListLoadingStatus.EMPTY)
    val status = MutableStateFlow(LoadingStatuses.EMPTY)

    val query = MutableLiveData<String>()

    val movies = MutableLiveData<ArrayList<MovieEntity>>()

    private fun setStatus(value: LoadingStatuses) {
        //status.postValue(value)
        status.value = value
    }

    private fun getQuery(): String {
        return query.value ?: ""
    }

    fun setQuery(value: String) {
        query.postValue(value)
    }

    fun addMovie(movie: MovieEntity) {
        val currentData = movies.value

        if (currentData == null) {
            val updatedData = ArrayList<MovieEntity>()
            updatedData.add(movie)

            movies.postValue(updatedData)
        }
        else {
            val updatedData = currentData.toMutableList().apply {
                add(movie)
            }

            movies.postValue(updatedData as ArrayList<MovieEntity>)
        }
    }

    private fun replaceMovies(list: List<MovieEntity>) {
        movies.postValue(list as ArrayList<MovieEntity>)
    }

    private fun clearMovies() {
        movies.postValue(ArrayList())
    }

    fun search() {
        setStatus(LoadingStatuses.LOADING)
        clearMovies()

        GlobalScope.launch(Dispatchers.IO) {
            val founded = useCase.search(getQuery()) as ArrayList<MovieEntity>

            if (founded.isEmpty()) {
                setStatus(LoadingStatuses.EMPTY)
            } else {
                replaceMovies(founded)
                setStatus(LoadingStatuses.LOADED)
            }
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