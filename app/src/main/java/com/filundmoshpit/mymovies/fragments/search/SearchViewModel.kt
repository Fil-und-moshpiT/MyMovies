package com.filundmoshpit.mymovies.fragments.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.filundmoshpit.mymovies.MainActivity
import com.filundmoshpit.mymovies.data.Movie
import com.filundmoshpit.mymovies.data.external.SearchResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchViewModel : ViewModel() {

    val searching = MutableLiveData<Boolean>()

    val query = MutableLiveData<String>()

    val movies = MutableLiveData<ArrayList<Movie>>()

    fun setSearching(value: Boolean) {
        searching.postValue(value)
    }

    private fun getQuery(): String {
        return query.value ?: ""
    }

    fun setQuery(value: String) {
        query.postValue(value)
    }

    fun addMovie(movie: Movie) {
        val currentData = movies.value

        if (currentData == null) {
            val updatedData = ArrayList<Movie>()
            updatedData.add(movie)

            movies.postValue(updatedData)
        }
        else {
            val updatedData = currentData.toMutableList().apply {
                add(movie)
            }

            movies.postValue(updatedData as ArrayList<Movie>)
        }
    }

    fun replaceMovies(list: List<Movie>) {
        movies.postValue(list as ArrayList<Movie>)
    }

    fun clearMovies() {
        movies.postValue(ArrayList())
    }

    fun search() {
        setSearching(true)
        clearMovies()

        val name = getQuery()

        GlobalScope.launch(Dispatchers.IO) {
            MainActivity.searchService.search(name).enqueue(
                object : Callback<SearchResponse> {
                    override fun onResponse(call: Call<SearchResponse>, response: Response<SearchResponse>) {
                        val searchResponse = response.body()?.docs

                        if (searchResponse != null) replaceMovies(searchResponse)

                        setSearching(false)
                    }

                    override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                        setSearching(false)
                    }
                }
            )
        }
    }
}