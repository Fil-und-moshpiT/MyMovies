package com.filundmoshpit.mymovies.domain.usecases

import com.filundmoshpit.mymovies.domain.MovieEntity
import com.filundmoshpit.mymovies.domain.MoviesRepository

class WatchLaterUseCase(private val repository: MoviesRepository) {
    fun load() : List<MovieEntity> {
        return repository.getWatchLater()
    }
}