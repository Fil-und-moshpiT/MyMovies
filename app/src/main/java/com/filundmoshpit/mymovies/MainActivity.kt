package com.filundmoshpit.mymovies

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.room.Room
import com.filundmoshpit.mymovies.data.MoviesRepositoryImpl
import com.filundmoshpit.mymovies.data.external.KinopoiskAPI
import com.filundmoshpit.mymovies.data.internal.MovieDAO
import com.filundmoshpit.mymovies.data.internal.MoviesDatabase
import com.filundmoshpit.mymovies.domain.FavouritesUseCase
import com.filundmoshpit.mymovies.domain.MoviesRepository
import com.filundmoshpit.mymovies.domain.SearchUseCase
import com.filundmoshpit.mymovies.domain.WatchLaterUseCase
import com.filundmoshpit.mymovies.presentation.favourites.FavouritesFragment
import com.filundmoshpit.mymovies.presentation.search.SearchFragment
import com.filundmoshpit.mymovies.presentation.watch_later.WatchLaterFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/*
TODO:
    +Add "Search" fragment (Retrofit)
    +Add Repository class for local and remote data sources
    +Add "Watch later" fragment (Room)
    +Add "Favourites" fragment (Room)
    +Add bottom navigation menu
    +Check MVVM
    Move all strings in resources
    Add translation
*/

class MainActivity : AppCompatActivity() {

    companion object {
        private lateinit var searchService: KinopoiskAPI
        private lateinit var databaseService: MovieDAO
        private lateinit var moviesRepository: MoviesRepository

        lateinit var searchUseCase: SearchUseCase
        lateinit var watchLaterUseCase: WatchLaterUseCase
        lateinit var favouritesUseCase: FavouritesUseCase
    }

    private lateinit var bottomNavigationView: BottomNavigationView
    private val fragmentSearch     = SearchFragment()
    private val fragmentWatchLater = WatchLaterFragment()
    private val fragmentFavourite  = FavouritesFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Data services configuration
        configureRetrofit()
        configureRoom()

        moviesRepository = MoviesRepositoryImpl(searchService, databaseService)

        searchUseCase     = SearchUseCase(moviesRepository)
        watchLaterUseCase = WatchLaterUseCase(moviesRepository)
        favouritesUseCase = FavouritesUseCase(moviesRepository)

        //Set search fragment as default
        openFragment(fragmentSearch)

        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_bar_item_search -> {
                    openFragment(fragmentSearch)
                    return@setOnItemSelectedListener true
                }
                R.id.navigation_bar_item_watch_later -> {
                    openFragment(fragmentWatchLater)
                    return@setOnItemSelectedListener true
                }
                R.id.navigation_bar_item_favourites -> {
                    openFragment(fragmentFavourite)
                    return@setOnItemSelectedListener true
                }
                else -> {
                    return@setOnItemSelectedListener false
                }
            }
        }
    }

    private fun openFragment(fragment: Fragment) {
        supportFragmentManager.commit {
            replace(R.id.fragment_main, fragment)
        }
    }

    private fun configureRetrofit() {
        val queryInterceptor = Interceptor {
            val url = it.request().url.newBuilder()
                .addQueryParameter("field", "name")
                //.addQueryParameter("limit", "2")
                .addQueryParameter("isStrict", "false")
                .addQueryParameter("token", BuildConfig.KINOPOISK_API_KEY).build()

            val request = it.request().newBuilder()
                .url(url)
                .build()

            it.proceed(request)
        }

        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val httpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(queryInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.kinopoisk.dev/")
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        searchService = retrofit.create(KinopoiskAPI::class.java)
    }

    private fun configureRoom() {
        val moviesDatabase = Room.databaseBuilder(applicationContext, MoviesDatabase::class.java, "movies-database").build()

        databaseService = moviesDatabase.movieDAO()
    }
}