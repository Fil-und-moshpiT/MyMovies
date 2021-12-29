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
    val imageSmall: String,
    val imageBig: String,
    val rating: Float,
    val favourite: Boolean,
    val watchLater: Boolean
) {
    constructor(movie: MovieEntity) : this(
        movie.id,
        movie.name,
        movie.description,
        movie.imageSmall,
        movie.imageBig,
        movie.rating,
        movie.favourite,
        movie.watchLater
    )

    fun toMovieEntity() =
        MovieEntity(id, name, description, imageSmall, imageBig, rating, favourite, watchLater)
}