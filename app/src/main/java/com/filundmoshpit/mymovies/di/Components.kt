package com.filundmoshpit.mymovies.di

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.filundmoshpit.mymovies.BuildConfig
import com.filundmoshpit.mymovies.data.MoviesRepositoryImpl
import com.filundmoshpit.mymovies.data.external.tmdb.ImageConfiguration
import com.filundmoshpit.mymovies.data.external.tmdb.TMDBApi
import com.filundmoshpit.mymovies.data.external.tmdb.TMDBMovieEntity
import com.filundmoshpit.mymovies.data.internal.MovieDAO
import com.filundmoshpit.mymovies.data.internal.MoviesDatabase
import com.filundmoshpit.mymovies.domain.MoviesRepository
import com.filundmoshpit.mymovies.presentation.MainActivity
import com.filundmoshpit.mymovies.presentation.favourites.FavouritesFragment
import com.filundmoshpit.mymovies.presentation.movie_card.MovieCardActivity
import com.filundmoshpit.mymovies.presentation.search.SearchFragment
import com.filundmoshpit.mymovies.presentation.watch_later.WatchLaterFragment
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.*
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Component(modules = [ExternalModule::class, InternalModule::class, BindModule::class])
@Singleton
interface ApplicationContextComponent {

    fun inject(activity: MainActivity)
    fun inject(activity: MovieCardActivity)

    fun inject(fragment: SearchFragment)
    fun inject(fragment: FavouritesFragment)
    fun inject(fragment: WatchLaterFragment)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun context(context: Context): Builder

        fun build(): ApplicationContextComponent
    }
}

@Component(modules = [ExternalModule::class])
@Singleton
interface ExternalComponent {

    fun inject(movieEntity: TMDBMovieEntity.GsonPosterDeserializer)
}

@Module
object ExternalModule {

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().serializeNulls().create()

    @Provides
    @Singleton
    fun provideOkHttp(): OkHttpClient {
        val queryInterceptor = Interceptor {
            val url = it.request().url.newBuilder()
                .addQueryParameter("language", MainActivity.settingsService.language)
                .addQueryParameter("api_key", BuildConfig.TMDB_API_KEY)
                .build()

            val request = it.request().newBuilder()
                .url(url)
                .build()

            it.proceed(request)
        }

        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(queryInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideTMDBApi(okHttpClient: OkHttpClient, gson: Gson): TMDBApi =
        Retrofit.Builder().baseUrl("https://api.themoviedb.org/3/").client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson)).build()
            .create(TMDBApi::class.java)

    @Provides
    @Singleton
    fun provideImageConfiguration(api: TMDBApi): ImageConfiguration {
        Log.d("DAGGER", "image configuration created")

        var url = ""
        var sizeSmall = ""
        var sizeBig = ""

        val response = api.configuration().execute()

        if (response.isSuccessful) {
            val configuration = response.body()?.configuration
            if (configuration != null) {
                url = configuration.url_secure.ifEmpty {
                    configuration.url
                }

                configuration.sizes.forEach {
                    try {
                        val currentSize = Integer.parseInt(it.substring(1))

                        if (sizeSmall.isEmpty() && currentSize >= 100) {
                            sizeSmall = it
                        }

                        if (sizeBig.isEmpty() && currentSize >= 500) {
                            sizeBig = it
                        }

                        if (sizeSmall.isNotEmpty() && sizeBig.isNotEmpty()) {
                            return@forEach
                        }
                    } catch (e: NumberFormatException) {

                    }
                }
            }
        }

        return ImageConfiguration(url, sizeSmall, sizeBig)
    }
}

@Module
object InternalModule {

    @Provides
    @Singleton
    fun provideInternal(context: Context): MovieDAO =
        Room.databaseBuilder(context, MoviesDatabase::class.java, "movies-database").build()
            .movieDAO()
}

@Module
interface BindModule {
    @Binds
    fun bindMoviesRepositoryImpl_to_MoviesRepository(moviesRepositoryImpl: MoviesRepositoryImpl): MoviesRepository
}