package com.filundmoshpit.mymovies.domain

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class SearchUseCase(private val repository: MoviesRepository) {
    fun search(query: String) : List<Movie> {
        return repository.search(query)
    }

    fun addFavourite(movie: Movie) {
        repository.addFavourite(movie)
    }

    fun addWatchLater(movie: Movie) {
        repository.addWatchLater(movie)
    }
}