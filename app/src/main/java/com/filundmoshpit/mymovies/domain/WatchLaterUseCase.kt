package com.filundmoshpit.mymovies.domain

class WatchLaterUseCase(private val repository: MoviesRepository) {

    fun load() : List<MovieEntity> {
        return repository.getWatchLater()
    }

    fun updateFavourite(movie: MovieEntity) {
        repository.updateFavourite(movie)
    }
}