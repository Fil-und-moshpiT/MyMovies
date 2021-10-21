package com.filundmoshpit.mymovies.data.internal

import androidx.room.*

@Dao
interface MovieDAO {

    @Insert
    fun insert(movie: InternalMovie)

    @Insert
    fun insert(movies: List<InternalMovie>)

//    @Delete
//    fun delete(movie: InternalMovie)

    @Query("SELECT * FROM internalmovie WHERE id = :id")
    fun getById(id: Int): List<InternalMovie>

    @Query("SELECT * FROM internalmovie WHERE internalmovie.favourite")
    fun getFavourites(): List<InternalMovie>

//    @Query("SELECT * FROM internalmovie")
//    fun getAll(): List<InternalMovie>

    @Query("UPDATE internalmovie SET favourite = :favourite WHERE id = :id")
    fun updateFavourite(id: Int, favourite: Boolean)

    fun setFavourite(movie: InternalMovie, favourite: Boolean) {
        val found = getById(movie.id)

        if (found.isEmpty()) {
            insert(movie)
        }

        updateFavourite(movie.id, favourite)
    }
}

@Database(entities = [InternalMovie::class], version = 1)
abstract class MoviesDatabase : RoomDatabase() {
    abstract fun movieDAO(): MovieDAO
}
