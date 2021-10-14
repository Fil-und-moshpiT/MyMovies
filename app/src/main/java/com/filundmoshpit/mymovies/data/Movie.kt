package com.filundmoshpit.mymovies.data

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import java.lang.reflect.Type

data class Movie(
    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("description")
    val description: String?,

    @SerializedName("poster")
    @JsonAdapter(PosterDeserializer::class)
    val image: String
)

object PosterDeserializer : JsonDeserializer<String> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): String? {
        return json?.run {
            asJsonObject
            .get("previewUrl")
            .asString
        }
    }
}