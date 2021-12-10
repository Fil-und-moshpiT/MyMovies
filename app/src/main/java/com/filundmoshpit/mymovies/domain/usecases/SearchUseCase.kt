package com.filundmoshpit.mymovies.domain.usecases

import com.filundmoshpit.mymovies.data.utils.ExternalResponse
import com.filundmoshpit.mymovies.domain.MovieEntity
import com.filundmoshpit.mymovies.domain.MoviesRepository

class SearchUseCase(private val repository: MoviesRepository) {
    fun search(query: String): ExternalResponse {
        return repository.search(query)
    }
}