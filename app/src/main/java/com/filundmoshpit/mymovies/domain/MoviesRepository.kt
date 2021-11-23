package com.filundmoshpit.mymovies.domain

import com.filundmoshpit.mymovies.data.utils.ExternalResponse

interface MoviesRepository {

    fun search(query: String) : ExternalResponse

    fun updateFavourite(movie: MovieEntity)

    fun getFavourite() : List<MovieEntity>

    fun updateWatchLater(movie: MovieEntity)

    fun getWatchLater() : List<MovieEntity>

    fun getMovieByID(id: Int): MovieEntity
}