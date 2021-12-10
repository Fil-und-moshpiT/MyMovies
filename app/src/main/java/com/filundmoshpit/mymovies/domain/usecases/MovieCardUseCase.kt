package com.filundmoshpit.mymovies.domain.usecases

import com.filundmoshpit.mymovies.domain.MovieEntity
import com.filundmoshpit.mymovies.domain.MoviesRepository

class MovieCardUseCase(private val repository: MoviesRepository) {
    fun getMovieByID(id: Int): MovieEntity {
        return repository.getMovieByID(id)
    }

    fun updateFavourite(movie: MovieEntity) {
        repository.updateFavourite(movie)
    }

    fun updateWatchLater(movie: MovieEntity) {
        repository.updateWatchLater(movie)
    }
}