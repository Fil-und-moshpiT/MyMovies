package com.filundmoshpit.mymovies.data

import com.filundmoshpit.mymovies.data.external.KinopoiskAPI
import com.filundmoshpit.mymovies.data.external.SearchResponse
import com.filundmoshpit.mymovies.data.internal.InternalMovie
import com.filundmoshpit.mymovies.data.internal.MovieDAO
import com.filundmoshpit.mymovies.domain.Movie
import com.filundmoshpit.mymovies.domain.MoviesRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MoviesRepositoryImpl(private val external: KinopoiskAPI, private val internal: MovieDAO) : MoviesRepository {
    override fun search(query: String) : List<Movie> {
        val result = ArrayList<Movie>()

        val response = external.search(query).execute()

        if (response.isSuccessful) {
            val searchResponse = response.body()?.docs

            if (searchResponse != null) {
                for (externalMovie in searchResponse) {
                    result.add(externalMovie.toMovie())
                }
            }
        }

        /*kinopoiskAPI.search(query).enqueue(
            object : Callback<SearchResponse> {
                override fun onResponse(call: Call<SearchResponse>, response: Response<SearchResponse>) {
                    val searchResponse = response.body()?.docs

                    if (searchResponse != null) {
                        for (externalMovie in searchResponse) {
                            result.add(externalMovie.toMovie())
                        }
                    }
                }

                override fun onFailure(call: Call<SearchResponse>, t: Throwable) {}
            }
        )*/

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

    override fun addFavourite(movie: Movie) {
        val internalMovie = InternalMovie(
            movie.getID(),
            movie.getName(),
            movie.getDescription(),
            movie.getImage(),
            HashMap(),
            movie.getFavourite(),
            movie.getWatchLater()
        )

        internal.setFavourite(internalMovie, true)
    }

    override fun getFavourites(): List<Movie> {
        val result = ArrayList<Movie>()
        val internalMovies = internal.getFavourites()

        for (internalMovie in internalMovies) {
            result.add(internalMovie.toMovie())
        }

        return result
    }

    override fun addWatchLater(movie: Movie) {
        TODO("Not yet implemented")
    }

    override fun getWatchLater(): List<Movie> {
        TODO("Not yet implemented")
    }
}