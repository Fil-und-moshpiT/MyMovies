package com.filundmoshpit.mymovies.domain

class SearchUseCase(private val repository: MoviesRepository) {
    fun search(query: String): List<MovieEntity> {
        return repository.search(query)
    }

    fun updateFavourite(movie: MovieEntity) {
        repository.updateFavourite(movie)
    }

    fun updateWatchLater(movie: MovieEntity) {
        repository.updateWatchLater(movie)
    }
}