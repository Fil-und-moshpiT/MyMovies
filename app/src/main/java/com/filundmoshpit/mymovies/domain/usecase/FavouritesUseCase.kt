package com.filundmoshpit.mymovies.domain.usecase

import com.filundmoshpit.mymovies.domain.MovieEntity
import com.filundmoshpit.mymovies.domain.MoviesRepository
import javax.inject.Inject

class FavouritesUseCase @Inject constructor(private val repository: MoviesRepository) {

    fun load() : List<MovieEntity> {
        return repository.getFavourite()
    }
}