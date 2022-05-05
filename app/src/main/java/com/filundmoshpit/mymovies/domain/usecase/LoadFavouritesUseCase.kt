package com.filundmoshpit.mymovies.domain.usecase

import com.filundmoshpit.mymovies.domain.MovieEntity
import com.filundmoshpit.mymovies.domain.MoviesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoadFavouritesUseCase @Inject constructor(private val repository: MoviesRepository) {
    fun load() : Flow<List<MovieEntity>> = repository.getFavourite()
}