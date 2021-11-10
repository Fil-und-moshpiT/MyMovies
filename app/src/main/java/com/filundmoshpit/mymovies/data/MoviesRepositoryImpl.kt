package com.filundmoshpit.mymovies.data

import com.filundmoshpit.mymovies.data.external.KinopoiskAPI
import com.filundmoshpit.mymovies.data.internal.InternalMovie
import com.filundmoshpit.mymovies.data.internal.MovieDAO
import com.filundmoshpit.mymovies.domain.MovieEntity
import com.filundmoshpit.mymovies.domain.MoviesRepository

class MoviesRepositoryImpl(private val external: KinopoiskAPI, private val internal: MovieDAO) : MoviesRepository {
    override fun search(query: String) : List<MovieEntity> {
        val result = ArrayList<MovieEntity>()

        val response = external.search(query).execute()

        if (response.isSuccessful) {
            val searchResponse = response.body()?.docs

            if (searchResponse != null) {
                for (externalMovie in searchResponse) {
                    result.add(externalMovie.toMovie())
                }
            }
        }

        for (movie in result) {
            var favourite = false
            var watchlater = false

            val found = internal.getById(movie.getID())
            if (found.isNotEmpty()) {
                favourite = found[0].favourite
                watchlater = found[0].watchLater
            }

            movie.setFavourite(favourite)
            movie.setWatchLater(watchlater)
        }

        return result
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

    override fun getFavourites(): List<MovieEntity> {
        val result = ArrayList<MovieEntity>()
        val internalMovies = internal.getFavourites()

        for (internalMovie in internalMovies) {
            result.add(internalMovie.toMovie())
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
            result.add(internalMovie.toMovie())
        }

        return result
    }
}