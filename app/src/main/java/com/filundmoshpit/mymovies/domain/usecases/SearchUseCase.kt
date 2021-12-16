package com.filundmoshpit.mymovies.domain.usecases

import com.filundmoshpit.mymovies.data.external.ExternalResponse
import com.filundmoshpit.mymovies.domain.MoviesRepository

class SearchUseCase(private val repository: MoviesRepository) {
    fun search(query: String): ExternalResponse {
        return repository.search(query)
    }
}