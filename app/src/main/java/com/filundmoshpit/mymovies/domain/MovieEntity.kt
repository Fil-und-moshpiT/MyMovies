package com.filundmoshpit.mymovies.domain

//Using in UI lists
class MovieEntity(
    val id: Int,
    val name: String,
    val description: String,
    val image: String,
    val rating: Float
) {
    private var favourite: Boolean = false
    private var watchLater: Boolean = false

    fun getFavourite() = favourite

    fun getWatchLater() = watchLater

    fun changeFavourite() {
        favourite = !favourite
    }

    fun setFavourite(value: Boolean) {
        favourite = value
    }

    fun changeWatchLater() {
        watchLater = !watchLater
    }

    fun setWatchLater(value: Boolean) {
        watchLater = value
    }
}