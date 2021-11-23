package com.filundmoshpit.mymovies.data.utils

import com.filundmoshpit.mymovies.data.external.KinopoiskAPI
import com.filundmoshpit.mymovies.data.internal.InternalMovie
import com.filundmoshpit.mymovies.data.internal.MovieDAO
import com.filundmoshpit.mymovies.domain.MovieEntity
import com.filundmoshpit.mymovies.domain.MoviesRepository
import java.lang.Exception
import java.net.UnknownHostException

class MoviesRepositoryImpl(private val external: KinopoiskAPI, private val internal: MovieDAO) : MoviesRepository {
    override fun search(query: String) : ExternalResponse {
        try {
            val response = external.search(query).execute()

            if (response.isSuccessful) {
                val searchResponse = response.body()?.docs

                if (searchResponse == null) {
                    return ExternalError("Unknown response")
                }
                else {
                    val movies = ArrayList<MovieEntity>()

                    for (externalMovie in searchResponse) {
                        val movie = externalMovie.toMovie()

                        var favourite = false
                        var watchlater = false

                        val found = internal.getById(movie.getID())
                        if (found.isNotEmpty()) {
                            favourite = found[0].favourite
                            watchlater = found[0].watchLater
                        }

                        movie.setFavourite(favourite)
                        movie.setWatchLater(watchlater)

                        movies.add(movie)
                    }

                    return ExternalSuccess(movies)
                }
            }
            else {
                return ExternalError(response.message())
            }
        }
        catch (e: UnknownHostException) {
            return ExternalError("Connection error")
        }
        catch (e: Exception) {
            return ExternalError(e.toString())
        }
    }

    override fun updateFavourite(movie: MovieEntity) {
        val internalMovie = InternalMovie(
            movie.getID(),
            movie.getName(),
            movie.getDescription(),
            movie.getImage(),
            HashMap(),
            movie.getFavourite(),
            movie.getWatchLater()
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
            movie.getID(),
            movie.getName(),
            movie.getDescription(),
            movie.getImage(),
            HashMap(),
            movie.getFavourite(),
            movie.getWatchLater()
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

        return MovieEntity(0, "Unknown", "Unknown", "", HashMap())
    }
}