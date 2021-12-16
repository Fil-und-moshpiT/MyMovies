package com.filundmoshpit.mymovies

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.preference.PreferenceManager
import androidx.room.Room
import com.filundmoshpit.mymovies.data.external.tmdb.TMDBApi
import com.filundmoshpit.mymovies.data.internal.MovieDAO
import com.filundmoshpit.mymovies.data.internal.MoviesDatabase
import com.filundmoshpit.mymovies.data.MoviesRepositoryImpl
import com.filundmoshpit.mymovies.databinding.ActivityMainBinding
import com.filundmoshpit.mymovies.domain.MoviesRepository
import com.filundmoshpit.mymovies.domain.usecases.FavouritesUseCase
import com.filundmoshpit.mymovies.domain.usecases.MovieCardUseCase
import com.filundmoshpit.mymovies.domain.usecases.SearchUseCase
import com.filundmoshpit.mymovies.domain.usecases.WatchLaterUseCase
import com.google.android.material.navigation.NavigationBarView
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*


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
    +Add translation
    +Add Jetpack Navigation
    +Add network to database caching while search
    +Add ViewBinding
    Add animations
    +Show/hide bottom navigation bar
    +Add stars rating
    +Add TMDB
    ?ADD OMDB
    Add constructors to internal/external movies
    +Remove Kinopoisk API
    +Add universal view holder
    +Add EventBus to fragments (update watch later & favourite)
    +Add settings fragment
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

        lateinit var settingsService: Settings
    }

    private lateinit var navigationController: NavController

    override fun attachBaseContext(baseContext: Context?) {
        var newContext = baseContext

        if (baseContext != null) {
            settingsService = Settings(PreferenceManager.getDefaultSharedPreferences(newContext))

            //Load settings

            //Dark theme
            val systemDarkTheme = when (baseContext.resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                Configuration.UI_MODE_NIGHT_YES -> true
                else -> false
            }
            val currentDarkTheme = settingsService.darkTheme

            //Change night mode
            if (systemDarkTheme != currentDarkTheme) {
                val nightMode = if (currentDarkTheme) MODE_NIGHT_YES else MODE_NIGHT_NO

                AppCompatDelegate.setDefaultNightMode(nightMode)
            }

            //Language
            val systemLanguage = Locale.getDefault().language
            val currentLanguage = settingsService.language

            //Change language
            if (systemLanguage != currentLanguage) {
                val configuration = baseContext.resources.configuration
                configuration.setLocale(Locale.forLanguageTag(currentLanguage))

                newContext = baseContext.createConfigurationContext(configuration)
            }
        }

        super.attachBaseContext(newContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Data services configuration
        configureTMDBApi()
        configureInternalDB()

        //Data
        moviesRepository = MoviesRepositoryImpl(tmdbService, databaseService)

        movieCardUseCase  = MovieCardUseCase(moviesRepository)
        searchUseCase     = SearchUseCase(moviesRepository)
        watchLaterUseCase = WatchLaterUseCase(moviesRepository)
        favouritesUseCase = FavouritesUseCase(moviesRepository)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Navigation
        navigationController =
            (supportFragmentManager.findFragmentById(R.id.navigation_host_fragment) as NavHostFragment).navController

        ModifiedNavigationUI.setupWithNavController(binding.navigationMenu as NavigationBarView, navigationController)
    }

    private fun configureTMDBApi() {
        val queryInterceptor = Interceptor {
            val url = it.request().url.newBuilder()
                .addQueryParameter("language", settingsService.language)
                .addQueryParameter("api_key", BuildConfig.TMDB_API_KEY)
                .build()

            val request = it.request().newBuilder()
                .url(url)
                .build()

            it.proceed(request)
        }

//        val loggingInterceptor = HttpLoggingInterceptor()
//        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val httpClient = OkHttpClient.Builder()
//            .addInterceptor(loggingInterceptor)
            .addInterceptor(queryInterceptor)
            .build()

        val gson = GsonBuilder().serializeNulls().create()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        tmdbService = retrofit.create(TMDBApi::class.java)
    }

    private fun configureInternalDB() {
        val moviesDatabase = Room.databaseBuilder(applicationContext, MoviesDatabase::class.java, "movies-database").build()

        databaseService = moviesDatabase.movieDAO()
    }
}
