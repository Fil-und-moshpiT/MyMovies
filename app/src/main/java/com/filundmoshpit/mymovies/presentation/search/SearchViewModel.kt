package com.filundmoshpit.mymovies.presentation.search

import androidx.lifecycle.ViewModel
import com.filundmoshpit.mymovies.R
import com.filundmoshpit.mymovies.data.utils.ExternalError
import com.filundmoshpit.mymovies.data.utils.ExternalSuccess
import com.filundmoshpit.mymovies.domain.MovieEntity
import com.filundmoshpit.mymovies.domain.SearchUseCase
import com.filundmoshpit.mymovies.presentation.util.LoadingStatuses
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(private val useCase: SearchUseCase) : ViewModel() {

    val status = MutableStateFlow(LoadingStatuses.EMPTY)

    val errorTextId = MutableStateFlow(ERROR_STRING_ID_EMPTY)

    private val query = MutableStateFlow("")

    val movies = MutableStateFlow(ArrayList<MovieEntity>())

    private fun setStatus(value: LoadingStatuses) {
        status.value = value
    }

    private fun setError(value: Int) {
        errorTextId.value = value
    }

    fun getQuery(): String {
        return query.value
    }

    fun setQuery(value: String) {
        query.value = value
    }

    /*fun addMovie(movie: MovieEntity) {
        movies.value = movies.value.toMutableList().apply { add(movie) } as ArrayList<MovieEntity>
    }*/

    private fun replaceMovies(list: List<MovieEntity>) {
        movies.value = list as ArrayList<MovieEntity>
    }

    private fun clearMovies() {
        movies.value.clear()
    }

    fun search() {
        setStatus(LoadingStatuses.LOADING)
        clearMovies()

        GlobalScope.launch(Dispatchers.IO) {
            when (val response = useCase.search(getQuery())) {
                is ExternalSuccess -> {
                    val loaded = response.getMovies()

                    if (loaded.isNotEmpty()) {
                        replaceMovies(loaded)
                        setStatus(LoadingStatuses.LOADED)
                    }
                    else {
                        setError(ERROR_STRING_ID_EMPTY)
                        setStatus(LoadingStatuses.EMPTY)
                    }
                }
                is ExternalError -> {
                    setError(ERROR_STRING_ID_NETWORK)
                    setStatus(LoadingStatuses.EMPTY)
                }
            }
        }
    }

    companion object {
        const val ERROR_STRING_ID_EMPTY = R.string.search_error_empty_list
        const val ERROR_STRING_ID_NETWORK = R.string.search_error_network
    }
}