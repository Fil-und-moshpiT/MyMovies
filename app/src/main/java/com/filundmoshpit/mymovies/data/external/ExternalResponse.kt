package com.filundmoshpit.mymovies.data.external

import com.filundmoshpit.mymovies.domain.MovieEntity

sealed class ExternalResponse {

    class ExternalSuccess(private val movies: ArrayList<MovieEntity>) : ExternalResponse() {
        fun getMovies(): List<MovieEntity> {
            return movies
        }
    }

    object ExternalError : ExternalResponse()
}