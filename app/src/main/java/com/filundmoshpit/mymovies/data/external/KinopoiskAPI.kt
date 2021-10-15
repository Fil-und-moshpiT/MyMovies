package com.filundmoshpit.mymovies.data.external

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface KinopoiskAPI {

    @GET("movie")
    fun search(@Query("search") name: String): Call<SearchResponse>
}