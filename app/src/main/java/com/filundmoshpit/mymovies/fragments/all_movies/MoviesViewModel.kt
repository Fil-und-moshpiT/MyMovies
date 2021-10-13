package com.filundmoshpit.mymovies.fragments.all_movies

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.filundmoshpit.mymovies.data.Movie

class MoviesViewModel : ViewModel() {
    var liveData = MutableLiveData<ArrayList<Movie>>()

    fun add(movie: Movie) {
        val currentData = liveData.value

        if (currentData == null) {
            val updatedData = ArrayList<Movie>()
            updatedData.add(movie)

            liveData.postValue(updatedData)
        }
        else {
            val updatedData = currentData.toMutableList()
            updatedData.add(movie)

            liveData.postValue(updatedData as ArrayList<Movie>)
        }
    }
}