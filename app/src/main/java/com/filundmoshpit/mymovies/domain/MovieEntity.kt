package com.filundmoshpit.mymovies.domain

import android.os.Parcel
import android.os.Parcelable

//Using in UI lists
class MovieEntity(
    private val id: Int,
    private val name: String,
    private val description: String,
    private val image: String,
    private val rating: HashMap<String, Int>
) {
    private var favourite: Boolean = false
    private var watchLater: Boolean = false

    fun getID() = id

    fun getName() = name

    fun getDescription() = description

    fun getImage() = image

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