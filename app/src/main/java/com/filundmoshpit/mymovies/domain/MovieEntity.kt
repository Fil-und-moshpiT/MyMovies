package com.filundmoshpit.mymovies.domain

//Uses in UI lists
class MovieEntity(
    val id: Int,
    val name: String,
    val description: String,
    val imageSmall: String,
    val imageBig: String,
    val rating: Float,
    var favourite: Boolean = false,
    var watchLater: Boolean = false
) {
    fun changeFavourite() {
        favourite = !favourite
    }

    fun changeWatchLater() {
        watchLater = !watchLater
    }
}