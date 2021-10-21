package com.filundmoshpit.mymovies.domain

class FavouritesUseCase(private val repository: MoviesRepository) {

    fun load() : List<Movie> {
        return repository.getFavourites()
    }
}