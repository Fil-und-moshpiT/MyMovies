package com.filundmoshpit.mymovies.data.internal

import androidx.room.*
import com.filundmoshpit.mymovies.data.Movie

@Dao
interface MovieDAO {

    @Insert
    fun insert(movies: List<Movie>)

    @Delete
    fun delete(movie: Movie)

    @Query("SELECT * FROM movie")
    fun getAll(): List<Movie>

    @Query("SELECT * FROM movie WHERE id = :id")
    fun getById(id: Int): List<Movie>
}

@Database(entities = [Movie::class], version = 1)
abstract class MoviesDatabase : RoomDatabase() {
    abstract fun movieDAO(): MovieDAO
}
