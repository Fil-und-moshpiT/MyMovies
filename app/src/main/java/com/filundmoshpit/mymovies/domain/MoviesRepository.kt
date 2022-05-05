package com.filundmoshpit.mymovies.domain

import com.filundmoshpit.mymovies.data.external.ExternalResponse
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {

    suspend fun search(query: String) : ExternalResponse

    fun updateFavourite(movie: MovieEntity)

    fun getFavourite() : Flow<List<MovieEntity>>

    fun updateWatchLater(movie: MovieEntity)

    fun getWatchLater() : Flow<List<MovieEntity>>

    fun getMovieByID(id: Int): MovieEntity

    fun insertMovie(movie: MovieEntity)
}