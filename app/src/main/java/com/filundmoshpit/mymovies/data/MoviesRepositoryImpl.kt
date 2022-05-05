package com.filundmoshpit.mymovies.data

import com.filundmoshpit.mymovies.data.external.ExternalResponse
import com.filundmoshpit.mymovies.data.external.tmdb.TMDBApi
import com.filundmoshpit.mymovies.data.internal.InternalMovie
import com.filundmoshpit.mymovies.data.internal.MovieDAO
import com.filundmoshpit.mymovies.domain.MovieEntity
import com.filundmoshpit.mymovies.domain.MoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.net.UnknownHostException
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(
    private val external: TMDBApi,
    private val internal: MovieDAO
) : MoviesRepository {

    override suspend fun search(query: String): ExternalResponse {
        try {
            val movies = ArrayList<MovieEntity>()
            val response = external.search(query)

            response.results.forEach {
                movies.add(it.toMovie())
            }

            return ExternalResponse.ExternalSuccess(movies)
        }
        catch (e: UnknownHostException) {
            return ExternalResponse.ExternalError
        }
        catch (e: Exception) {
            return ExternalResponse.ExternalError
        }
    }

    override fun updateFavourite(movie: MovieEntity) {
        val internalMovie = InternalMovie(
            movie.id,
            movie.name,
            movie.description,
            movie.imageSmall,
            movie.imageBig,
            movie.rating,
            movie.favourite,
            movie.watchLater
        )

        internal.updateFavourite(internalMovie)
    }

    override fun getFavourite(): Flow<List<MovieEntity>> {
        val internalMovies = internal.getFavourites()

        return internalMovies.map { list ->
            list.map(InternalMovie::toMovieEntity)
        }
    }

    override fun updateWatchLater(movie: MovieEntity) {
        val internalMovie = InternalMovie(
            movie.id,
            movie.name,
            movie.description,
            movie.imageSmall,
            movie.imageBig,
            movie.rating,
            movie.favourite,
            movie.watchLater
        )

        internal.updateWatchLater(internalMovie)
    }

    override fun getWatchLater(): Flow<List<MovieEntity>> {
        val internalMovies = internal.getWatchLater()

        return internalMovies.map { list ->
            list.map(InternalMovie::toMovieEntity)
        }
    }

    override fun getMovieByID(id: Int): MovieEntity {
        val found = internal.getById(id)

        if (found.isNotEmpty()) {
            return found[0].toMovieEntity()
        }

        return MovieEntity(0, "Unknown", "Unknown", "", "", 0F)
    }

    override fun insertMovie(movie: MovieEntity) {
        val found = internal.getById(movie.id)
        if (found.isEmpty()) {
            internal.insert(InternalMovie(movie))
        }
    }
}