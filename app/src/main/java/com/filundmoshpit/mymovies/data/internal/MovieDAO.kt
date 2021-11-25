package com.filundmoshpit.mymovies.data.internal

import androidx.room.*

@Dao
interface MovieDAO {

    @Insert
    fun insertRequest(movie: InternalMovie)

    fun insert(movie: InternalMovie) {
        insertRequest(movie)
    }

//    @Insert
//    fun insert(movies: List<InternalMovie>)

//    @Delete
//    fun delete(movie: InternalMovie)

    @Query("SELECT * FROM internalmovie WHERE id = :id")
    fun getById(id: Int): List<InternalMovie>

    @Query("SELECT * FROM internalmovie WHERE internalmovie.favourite")
    fun getFavourites(): List<InternalMovie>

    @Query("UPDATE internalmovie SET favourite = :value WHERE id = :id")
    fun updateFavouriteRequest(id: Int, value: Boolean)

    fun updateFavourite(movie: InternalMovie) {
        val found = getById(movie.id)

        if (found.isEmpty()) {
            insertRequest(movie)
        }

        updateFavouriteRequest(movie.id, movie.favourite)
    }

    @Query("SELECT * FROM internalmovie WHERE internalmovie.watchLater")
    fun getWatchLater(): List<InternalMovie>

    @Query("UPDATE internalmovie SET watchLater = :value WHERE id = :id")
    fun updateWatchLaterRequest(id: Int, value: Boolean)

    fun updateWatchLater(movie: InternalMovie) {
        val found = getById(movie.id)

        if (found.isEmpty()) {
            insertRequest(movie)
        }

        updateWatchLaterRequest(movie.id, movie.watchLater)
    }
}

@Database(entities = [InternalMovie::class], version = 1)
abstract class MoviesDatabase : RoomDatabase() {
    abstract fun movieDAO(): MovieDAO
}
