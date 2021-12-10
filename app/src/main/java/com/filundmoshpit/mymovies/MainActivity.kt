package com.filundmoshpit.mymovies

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.room.Room
import com.filundmoshpit.mymovies.data.external.tmdb.TMDBApi
import com.filundmoshpit.mymovies.data.internal.MovieDAO
import com.filundmoshpit.mymovies.data.internal.MoviesDatabase
import com.filundmoshpit.mymovies.data.utils.MoviesRepositoryImpl
import com.filundmoshpit.mymovies.databinding.ActivityMainBinding
import com.filundmoshpit.mymovies.domain.*
import com.filundmoshpit.mymovies.domain.usecases.FavouritesUseCase
import com.filundmoshpit.mymovies.domain.usecases.MovieCardUseCase
import com.filundmoshpit.mymovies.domain.usecases.SearchUseCase
import com.filundmoshpit.mymovies.domain.usecases.WatchLaterUseCase
import com.google.gson.GsonBuilder
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
    +Add ViewBinding
    Add animations
    +Show/hide bottom navigation bar
    Add stars rating
    +Add TMDB
    ?ADD OMDB
    Add constructors to internal/external movies
    +Remove Kinopoisk API
    +Add universal view holder
    +Add EventBus to fragments (update watch later & favourite)
    Add settings fragment
*/

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    companion object {
        private lateinit var tmdbService: TMDBApi
        private lateinit var databaseService: MovieDAO
        private lateinit var moviesRepository: MoviesRepository

        lateinit var movieCardUseCase: MovieCardUseCase
        lateinit var searchUseCase: SearchUseCase
        lateinit var watchLaterUseCase: WatchLaterUseCase
        lateinit var favouritesUseCase: FavouritesUseCase
    }

    private lateinit var navigationController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)

        //Data services configuration
        configureTMDBApi()
        configureInternalDB()

        moviesRepository = MoviesRepositoryImpl(tmdbService, databaseService)

        movieCardUseCase  = MovieCardUseCase(moviesRepository)
        searchUseCase     = SearchUseCase(moviesRepository)
        watchLaterUseCase = WatchLaterUseCase(moviesRepository)
        favouritesUseCase = FavouritesUseCase(moviesRepository)

        //Navigation
        navigationController = (supportFragmentManager.findFragmentById(R.id.navigation_host_fragment) as NavHostFragment).navController

        navigationController.addOnDestinationChangedListener { controller, destination, arguments ->
            when (destination.id) {
                R.id.nav_movie_card_fragment -> {
                    if (binding.bottomNavigationMenu.visibility == View.VISIBLE) {
                        binding.bottomNavigationMenu.visibility = View.INVISIBLE
                    }
                }
                else -> {
                    if (binding.bottomNavigationMenu.visibility == View.INVISIBLE) {
                        binding.bottomNavigationMenu.visibility = View.VISIBLE
                    }
                }
            }
        }

        binding.bottomNavigationMenu.setupWithNavController(navigationController)
    }

    private fun configureTMDBApi() {
        val queryInterceptor = Interceptor {
            val url = it.request().url.newBuilder()
                //TODO: Add change during translation
                .addQueryParameter("language", "ru")
                .addQueryParameter("api_key", BuildConfig.TMDB_API_KEY)
                .build()

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

        val gson = GsonBuilder().serializeNulls().create()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .client(httpClient)
            //.addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        tmdbService = retrofit.create(TMDBApi::class.java)
    }

    private fun configureInternalDB() {
        val moviesDatabase = Room.databaseBuilder(applicationContext, MoviesDatabase::class.java, "movies-database").build()

        databaseService = moviesDatabase.movieDAO()
    }
}
