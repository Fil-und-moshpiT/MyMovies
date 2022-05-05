package com.filundmoshpit.mymovies.domain.usecase

import com.filundmoshpit.mymovies.domain.MovieEntity
import com.filundmoshpit.mymovies.domain.MoviesRepository
import javax.inject.Inject

class ReadMovieFromInternalUseCase @Inject constructor(private val repository: MoviesRepository) {
    fun getByID(id: Int): MovieEntity {
        return repository.getMovieByID(id)
    }
}