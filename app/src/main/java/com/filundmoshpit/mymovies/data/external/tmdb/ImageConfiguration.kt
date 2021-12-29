package com.filundmoshpit.mymovies.data.external.tmdb

import javax.inject.Singleton

@Singleton
data class ImageConfiguration(
    val url: String,
    val sizeSmall: String,
    val sizeBig: String
)