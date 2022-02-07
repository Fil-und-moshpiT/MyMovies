package com.filundmoshpit.mymovies.data

import com.filundmoshpit.mymovies.data.external.ExternalResponse
import com.filundmoshpit.mymovies.data.external.tmdb.TMDBApi
import com.filundmoshpit.mymovies.data.internal.InternalMovie
import com.filundmoshpit.mymovies.data.internal.MovieDAO
import com.filundmoshpit.mymovies.domain.MovieEntity
import com.filundmoshpit.mymovies.domain.MoviesRepository
import java.net.UnknownHostException
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(private val external: TMDBApi, private val internal: MovieDAO) : MoviesRepository {
    override suspend fun search(query: String) : ExternalResponse {
        try {
            val response = external.search(query)

            val movies = ArrayList<MovieEntity>()

            for (externalMovie in response.results) {
                val movie = externalMovie.toMovie()

                //Create instance of movie in local DB
                val found = internal.getById(movie.id)
                if (found.isEmpty()) {
                    internal.insert(InternalMovie(movie))
                }

                movies.add(movie)
            }

            return ExternalResponse.ExternalSuccess(movies)

            /*if (response.isSuccessful) {
                val searchResponse = response.body()?.results

                if (searchResponse == null) {
                    return ExternalResponse.ExternalError
                }
                else {
                    val movies = ArrayList<MovieEntity>()

                    for (externalMovie in searchResponse) {
                        val movie = externalMovie.toMovie()

                        //Create instance of movie in local DB
                        val found = internal.getById(movie.id)
                        if (found.isEmpty()) {
                            internal.insert(InternalMovie(movie))
                        }

                        movies.add(movie)
                    }

                    return ExternalResponse.ExternalSuccess(movies)
                }
            }
            else {
                return ExternalResponse.ExternalError
            }*/
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

    override fun getFavourite(): List<MovieEntity> {
        val result = ArrayList<MovieEntity>()
        val internalMovies = internal.getFavourites()

        for (internalMovie in internalMovies) {
            result.add(internalMovie.toMovieEntity())
        }

        return result
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

    override fun getWatchLater(): List<MovieEntity> {
        val result = ArrayList<MovieEntity>()
        val internalMovies = internal.getWatchLater()

        for (internalMovie in internalMovies) {
            result.add(internalMovie.toMovieEntity())
        }

        return result
    }

    override fun getMovieByID(id: Int): MovieEntity {
        val found = internal.getById(id)

        if (found.isNotEmpty()) {
            return found[0].toMovieEntity()
        }

        return MovieEntity(0, "Unknown", "Unknown", "", "", 0F)
    }
}