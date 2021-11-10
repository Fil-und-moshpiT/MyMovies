package com.filundmoshpit.mymovies.domain

class FavouritesUseCase(private val repository: MoviesRepository) {

    fun load() : List<MovieEntity> {
        return repository.getFavourites()
    }
}