package com.filundmoshpit.mymovies.domain

class MovieCardUseCase(private val repository: MoviesRepository) {
    fun getMovieByID(id: Int): MovieEntity {
        return repository.getMovieByID(id)
    }

    fun updateFavourite(movie: MovieEntity) {
        repository.updateFavourite(movie)
    }

    fun updateWatchLater(movie: MovieEntity) {
        repository.updateWatchLater(movie)
    }
}