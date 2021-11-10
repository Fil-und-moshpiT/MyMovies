package com.filundmoshpit.mymovies.presentation.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.filundmoshpit.mymovies.domain.MovieEntity
import com.filundmoshpit.mymovies.domain.SearchUseCase
import com.filundmoshpit.mymovies.presentation.util.ListLoadingStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(private val useCase: SearchUseCase) : ViewModel() {

    //val status = MutableLiveData(ListLoadingStatus.EMPTY)
    val status = MutableStateFlow(ListLoadingStatus.EMPTY)

    val query = MutableLiveData<String>()

    val movies = MutableLiveData<ArrayList<MovieEntity>>()

    private fun setStatus(value: ListLoadingStatus) {
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
        setStatus(ListLoadingStatus.LOADING)
        clearMovies()

        GlobalScope.launch(Dispatchers.IO) {
                /*MainActivity.searchService.search(name).enqueue(
                object : Callback<SearchResponse> {
                    override fun onResponse(call: Call<SearchResponse>, response: Response<SearchResponse>) {
                        val searchResponse = response.body()?.docs

                        if (searchResponse != null) {
                            val movies = ArrayList<Movie>()

                            for (externalMovie in searchResponse) {
                                movies.add(externalMovie.toMovie())
                            }

                            replaceMovies(movies)
                            setStatus(SearchStatus.LOADED)
                        }
                        else {
                            setStatus(SearchStatus.EMPTY)
                        }
                    }

                    override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                        setStatus(SearchStatus.EMPTY)
                    }
                }
            )*/

            val founded = useCase.search(getQuery()) as ArrayList<MovieEntity>

            if (founded.isEmpty()) {
                setStatus(ListLoadingStatus.EMPTY)
            } else {
                replaceMovies(founded)
                setStatus(ListLoadingStatus.LOADED)
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