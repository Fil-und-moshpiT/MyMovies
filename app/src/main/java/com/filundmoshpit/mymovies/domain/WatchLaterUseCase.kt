package com.filundmoshpit.mymovies.domain

class WatchLaterUseCase(private val repository: MoviesRepository) {

    fun load() : List<Movie> {
        return repository.getWatchLater()
    }

    fun updateFavourite(movie: Movie) {
        repository.updateFavourite(movie)
    }
}