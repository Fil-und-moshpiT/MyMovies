package com.filundmoshpit.mymovies.data.internal

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.filundmoshpit.mymovies.domain.MovieEntity

@Entity
data class InternalMovie(
    @PrimaryKey
    val id: Int,
    val name: String,
    val description: String,
    val image: String,
    val rating: Float,
    val favourite: Boolean,
    val watchLater: Boolean
) {
    constructor(movie: MovieEntity) : this(
        movie.id,
        movie.name,
        movie.description,
        movie.image,
        movie.rating,
        movie.getFavourite(),
        movie.getWatchLater()
    )

    fun toMovieEntity(): MovieEntity {
        val result = MovieEntity(id, name, description, image, rating)
        result.setFavourite(favourite)
        result.setWatchLater(watchLater)

        return result
    }
}