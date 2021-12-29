package com.filundmoshpit.mymovies.data.external.tmdb

import com.filundmoshpit.mymovies.di.DaggerExternalComponent
import com.filundmoshpit.mymovies.domain.MovieEntity
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import java.lang.reflect.Type
import javax.inject.Inject

data class TMDBMoviesResponse(val results: List<TMDBMovieEntity>)

data class TMDBMovieEntity(
    @SerializedName("id")
    val id: Int,

    @SerializedName("title")
    val name: String,

    @SerializedName("overview")
    val description: String?,

    @SerializedName("poster_path")
    @JsonAdapter(GsonPosterDeserializer::class)
    val images: Images?,

    @SerializedName("vote_average")
    val rating: Float
) {
    fun toMovie(): MovieEntity {
        return if (images == null)
            MovieEntity(id, name, description ?: "", "", "", rating)
        else
            MovieEntity(id, name, description ?: "", images.small, images.big, rating)
    }

    data class Images(
        val big: String = "",
        val small: String = ""
    )

    //Gson
    class GsonPosterDeserializer : JsonDeserializer<Images> {
        //DAGGER
        init {
            val daggerComponent = DaggerExternalComponent.builder().build()
            daggerComponent.inject(this)
        }

        @Inject
        lateinit var configuration: ImageConfiguration

        override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Images {
            val path = json?.asString

            if (path != null) {
                return Images(
                    "${configuration.url}${configuration.sizeBig}$path",
                    "${configuration.url}${configuration.sizeSmall}$path"
                )
            }

            return Images()
        }
    }
}