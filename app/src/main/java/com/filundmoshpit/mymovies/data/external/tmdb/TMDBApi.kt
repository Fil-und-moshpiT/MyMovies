package com.filundmoshpit.mymovies.data.external.tmdb

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface TMDBApi {
    // https://api.themoviedb.org/3/search/movie?api_key=<<api_key>>&language=en-US&include_adult=false
    @GET("search/movie")
    suspend fun search(@Query("query") query: String) : TMDBMoviesResponse
//    suspend fun search(@Query("query") query: String) : List<TMDBMovieEntity>

    //https://api.themoviedb.org/3/configuration?api_key=<<api_key>>
    @GET("configuration")
    fun configuration() : Call<TMDBConfigurationResponse>
}