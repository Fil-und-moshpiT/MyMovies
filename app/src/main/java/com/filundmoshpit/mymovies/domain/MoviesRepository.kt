package com.filundmoshpit.mymovies.domain

interface MoviesRepository {

    fun search(query: String) : List<Movie>

    fun updateFavourite(movie: Movie)

    fun getFavourites() : List<Movie>

    fun updateWatchLater(movie: Movie)

    fun getWatchLater() : List<Movie>
}