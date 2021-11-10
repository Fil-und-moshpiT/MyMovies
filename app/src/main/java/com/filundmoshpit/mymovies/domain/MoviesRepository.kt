package com.filundmoshpit.mymovies.domain

interface MoviesRepository {

    fun search(query: String) : List<MovieEntity>

    fun updateFavourite(movie: MovieEntity)

    fun getFavourites() : List<MovieEntity>

    fun updateWatchLater(movie: MovieEntity)

    fun getWatchLater() : List<MovieEntity>
}