package com.filundmoshpit.mymovies.domain

//Using in UI lists
class Movie(
    private val id: Int,
    private val name: String,
    private val description: String,
    private val image: String,
    private val rating: HashMap<String, Int>
) {
    private var favourite: Boolean = false
    private var watchLater: Boolean = false

    fun getID() : Int {
        return id
    }

    fun getName() : String {
        return name
    }

    fun getDescription() : String {
        return description
    }

    fun getImage() : String {
        return image
    }

    fun getFavourite() : Boolean {
        return favourite
    }

    fun setFavourite(value: Boolean) {
        favourite = value
    }

    fun getWatchLater() : Boolean {
        return watchLater
    }

    fun setWatchLater(value: Boolean) {
        watchLater = value
    }
}