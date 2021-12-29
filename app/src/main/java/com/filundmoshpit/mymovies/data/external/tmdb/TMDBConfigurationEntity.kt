package com.filundmoshpit.mymovies.data.external.tmdb

import com.google.gson.annotations.SerializedName

data class TMDBConfigurationResponse(
    @SerializedName("images")
    val configuration: TMDBConfigurationEntity
)

data class TMDBConfigurationEntity(
    @SerializedName("base_url")
    val url: String,

    @SerializedName("secure_base_url")
    val url_secure: String,

    @SerializedName("poster_sizes")
    val sizes: List<String>
)