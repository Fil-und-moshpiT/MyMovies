package com.filundmoshpit.mymovies.data.internal

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.filundmoshpit.mymovies.domain.MovieEntity

@Entity
@TypeConverters(InternalMovie.RoomRatingConverter::class)
data class InternalMovie(
    @PrimaryKey
    val id: Int,

    val name: String,

    val description: String,

    val image: String,

    val rating: HashMap<String, Int>,

    val favourite: Boolean,

    val watchLater: Boolean
) {
    /*constructor(movie: Movie) :
            this(movie.id,
                movie.name,
                movie.description ?: "",
                movie.image,
                movie.rating,
                false,
                false)*/

    fun toMovie(): MovieEntity {
        val result = MovieEntity(id, name, description, image, rating)
        result.setFavourite(favourite)
        result.setWatchLater(watchLater)

        return result
    }

    object RoomRatingConverter {
        @TypeConverter
        fun fromRating(ratings: HashMap<String, Int>): String {
            val strings = ArrayList<String>()

            for (entry in ratings) {
                strings.add("${entry.key}:${entry.value}")
            }

            return strings.joinToString()
        }

        @TypeConverter
        fun toRating(ratings: String): HashMap<String, Int> {
            val result = HashMap<String, Int>()

            for (entry in ratings.split(",")) {
                val keyvalue = entry.split(":")

                if (keyvalue.size == 2) {
                    result[keyvalue[0]] = keyvalue[1].toInt()
                }
            }

            return result
        }
    }
}