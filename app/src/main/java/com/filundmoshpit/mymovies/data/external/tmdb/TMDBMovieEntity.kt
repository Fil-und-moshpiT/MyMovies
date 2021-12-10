package com.filundmoshpit.mymovies.data.external.tmdb

import com.filundmoshpit.mymovies.domain.MovieEntity
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import java.lang.reflect.Type

data class TMDBMovieEntity(
    @SerializedName("id")
    val id: Int,

    @SerializedName("title")
    val name: String,

    @SerializedName("overview")
    val description: String?,

    @SerializedName("poster_path")
    @JsonAdapter(GsonPosterDeserializer::class)
    val image: String?,

    @SerializedName("vote_average")
    @JsonAdapter(GsonRatingDeserializer::class)
    val rating: HashMap<String, Int>
) {
    fun toMovie(): MovieEntity {
        return MovieEntity(id, name, description ?: "", image ?: "", rating)
    }

    //Gson
    object GsonPosterDeserializer : JsonDeserializer<String> {
        override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): String {
            val path = json?.asString

            if (path != null) {
                return "https://image.tmdb.org/t/p/w200$path"
            }

            return ""
        }
    }

    object GsonRatingDeserializer : JsonDeserializer<HashMap<String, Int>> {
        override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): HashMap<String, Int> {
            val result = HashMap<String, Int>()

            json?.run {
                result["imdb"] = this.asInt
            }

            return result
        }
    }
}

data class TMDBResponse(val results: List<TMDBMovieEntity>)
