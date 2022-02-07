package com.filundmoshpit.mymovies.domain.usecase

import com.filundmoshpit.mymovies.data.external.ExternalResponse
import com.filundmoshpit.mymovies.domain.MoviesRepository
import javax.inject.Inject

class SearchUseCase @Inject constructor(private val repository: MoviesRepository) {
    suspend fun search(query: String): ExternalResponse {
        return repository.search(query)
    }
}