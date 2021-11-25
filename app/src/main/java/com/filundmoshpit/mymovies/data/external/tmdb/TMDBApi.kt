package com.filundmoshpit.mymovies.data.external.tmdb

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TMDBApi {
    //https://api.themoviedb.org/3/movie/76341?api_key=<<api_key>>
    @GET("search/movie")
    fun search(@Query("query") query: String) : Call<TMDBResponse>
}