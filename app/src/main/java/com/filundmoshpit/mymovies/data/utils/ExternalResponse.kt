package com.filundmoshpit.mymovies.data.utils

import com.filundmoshpit.mymovies.domain.MovieEntity

sealed class ExternalResponse

class ExternalSuccess(private val movies: ArrayList<MovieEntity>) : ExternalResponse() {
    fun addMovie(movie: MovieEntity) {
        movies.add(movie)
    }

    fun getMovies() : List<MovieEntity> {
        return movies
    }
}

class ExternalError(private val info: String) : ExternalResponse() {
    fun getInfo() : String {
        return info
    }
}