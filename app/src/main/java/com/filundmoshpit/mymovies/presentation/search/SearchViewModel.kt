package com.filundmoshpit.mymovies.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.filundmoshpit.mymovies.R
import com.filundmoshpit.mymovies.data.external.ExternalResponse
import com.filundmoshpit.mymovies.domain.MovieEntity
import com.filundmoshpit.mymovies.domain.usecase.SearchUseCase
import com.filundmoshpit.mymovies.presentation.LoadingStatuses
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchViewModel(private val useCase: SearchUseCase) : ViewModel() {

    private val _status = MutableStateFlow(LoadingStatuses.EMPTY)
    private val _errorTextId = MutableStateFlow(ERROR_STRING_ID_EMPTY)
    private val _query = MutableStateFlow("")
    private val _movies = MutableStateFlow(ArrayList<MovieEntity>())

    val status: StateFlow<LoadingStatuses>
        get() = _status.asStateFlow()
    val errorTextId: StateFlow<Int>
        get() = _errorTextId.asStateFlow()
    val movies: StateFlow<ArrayList<MovieEntity>>
        get() = _movies.asStateFlow()

    private fun setStatus(value: LoadingStatuses) {
        _status.value = value
    }

    private fun setError(value: Int) {
        _errorTextId.value = value
    }

    fun getQuery(): String {
        return _query.value
    }

    fun setQuery(value: String) {
        _query.value = value
    }

    private fun replaceMovies(list: List<MovieEntity>) {
        _movies.value = list as ArrayList<MovieEntity>
    }

    private fun clearMovies() {
        _movies.value.clear()
    }

    fun search() {
        setStatus(LoadingStatuses.LOADING)
        clearMovies()

        viewModelScope.launch(Dispatchers.IO) {
            when (val response = useCase.search(getQuery())) {
                is ExternalResponse.ExternalSuccess -> {
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
                is ExternalResponse.ExternalError -> {
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

class SearchViewModelFactory @Inject constructor(private val useCase: SearchUseCase) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SearchViewModel(useCase) as T
    }
}