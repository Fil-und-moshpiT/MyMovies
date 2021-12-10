package com.filundmoshpit.mymovies

sealed interface BusEvents {
    object FavouriteChanged
    object WatchLaterChanged
}