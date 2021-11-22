package com.filundmoshpit.mymovies

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.room.Room
import com.filundmoshpit.mymovies.data.MoviesRepositoryImpl
import com.filundmoshpit.mymovies.data.external.KinopoiskAPI
import com.filundmoshpit.mymovies.data.internal.MovieDAO
import com.filundmoshpit.mymovies.data.internal.MoviesDatabase
import com.filundmoshpit.mymovies.domain.*
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
    +Add movie card fragment
    +Move all strings in resources
    Add translation
    +Add Jetpack Navigation
    +Add network to database caching while search
    Add animations
    Remove reloadings in watch later and favourites lists
*/

class MainActivity : AppCompatActivity() {

    companion object {
        private lateinit var searchService: KinopoiskAPI
        private lateinit var databaseService: MovieDAO
        private lateinit var moviesRepository: MoviesRepository

        lateinit var movieCardUseCase: MovieCardUseCase
        lateinit var searchUseCase: SearchUseCase
        lateinit var watchLaterUseCase: WatchLaterUseCase
        lateinit var favouritesUseCase: FavouritesUseCase
    }

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var navigationController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)

        //Data services configuration
        configureRetrofit()
        configureRoom()

        moviesRepository = MoviesRepositoryImpl(searchService, databaseService)

        movieCardUseCase  = MovieCardUseCase(moviesRepository)
        searchUseCase     = SearchUseCase(moviesRepository)
        watchLaterUseCase = WatchLaterUseCase(moviesRepository)
        favouritesUseCase = FavouritesUseCase(moviesRepository)

        //Navigation
        bottomNavigationView = findViewById(R.id.bottom_navigation_view)
        navigationController = (supportFragmentManager.findFragmentById(R.id.navigation_host_fragment) as NavHostFragment).navController

        NavigationUI.setupWithNavController(bottomNavigationView, navigationController)
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