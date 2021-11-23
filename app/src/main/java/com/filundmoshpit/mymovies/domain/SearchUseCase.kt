package com.filundmoshpit.mymovies.domain

import com.filundmoshpit.mymovies.data.utils.ExternalResponse

class SearchUseCase(private val repository: MoviesRepository) {
    fun search(query: String): ExternalResponse {
        return repository.search(query)
    }

    fun updateFavourite(movie: MovieEntity) {
        repository.updateFavourite(movie)
    }

    fun updateWatchLater(movie: MovieEntity) {
        repository.updateWatchLater(movie)
    }
}