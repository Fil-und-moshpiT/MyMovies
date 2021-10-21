package com.filundmoshpit.mymovies.domain

interface MoviesRepository {

    fun search(query: String) : List<Movie>

    fun addFavourite(movie: Movie)

    fun getFavourites() : List<Movie>

    fun addWatchLater(movie: Movie)

    fun getWatchLater() : List<Movie>
}