package com.filundmoshpit.mymovies.data.external

import com.filundmoshpit.mymovies.domain.MovieEntity
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import java.lang.reflect.Type

data class ExternalMovie(
    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("description")
    val description: String?,

    @SerializedName("poster")
    @JsonAdapter(GsonPosterDeserializer::class)
    val image: String,

    @SerializedName("rating")
    @JsonAdapter(GsonRatingDeserializer::class)
    val rating: HashMap<String, Int>
) {
    fun toMovie(): MovieEntity {
        val movie = MovieEntity(id, name, description ?: "", image, rating)

        return movie
    }

    //Gson
    object GsonPosterDeserializer : JsonDeserializer<String> {
        override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): String {
            return json?.run { asJsonObject.get("previewUrl").asString } ?: ""
        }
    }

    object GsonRatingDeserializer : JsonDeserializer<HashMap<String, Int>> {
        override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): HashMap<String, Int> {
            val result = HashMap<String, Int>()

            json?.run {
                val keys = asJsonObject.keySet()

                for (key in keys) {
                    result[key] = asJsonObject.get(key).asInt
                }
            }

            return result
        }
    }
}