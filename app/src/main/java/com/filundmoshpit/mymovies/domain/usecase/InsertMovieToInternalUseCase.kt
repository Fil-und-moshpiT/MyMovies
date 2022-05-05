package com.filundmoshpit.mymovies.domain.usecase

import com.filundmoshpit.mymovies.domain.MovieEntity
import com.filundmoshpit.mymovies.domain.MoviesRepository
import javax.inject.Inject

class InsertMovieToInternalUseCase @Inject constructor(private val repository: MoviesRepository) {
    fun insert(movie: MovieEntity) {
        repository.insertMovie(movie)
    }
}