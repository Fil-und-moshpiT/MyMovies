package com.filundmoshpit.mymovies.domain

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class SearchUseCase(private val repository: MoviesRepository) {
    fun search(query: String) : List<Movie> {
        return repository.search(query)
    }

    fun updateFavourite(movie: Movie) {
        repository.updateFavourite(movie)
    }

    fun updateWatchLater(movie: Movie) {
        repository.updateWatchLater(movie)
    }
}