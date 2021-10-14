package com.filundmoshpit.mymovies

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.filundmoshpit.mymovies.data.KinopoiskAPI
import com.filundmoshpit.mymovies.fragments.all_movies.AllMoviesFragment
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/*
TODO:
    Add "All movies" fragment (Retrofit)
    Add "Watch later" fragment (Room)
    Add "Liked" fragment (Room)
    Add bottom navigation menu
    Check MVVM
    Move all strings in resources
*/

class MainActivity : AppCompatActivity() {

    companion object {
        lateinit var searchService: KinopoiskAPI
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        configureRetrofit()

        //Set all movies fragment as default
        supportFragmentManager.commit {
            replace(R.id.f_all_movies, AllMoviesFragment.newInstance())
        }
    }

    private fun configureRetrofit() {
        val queryInterceptor = Interceptor {
            val url = it.request().url.newBuilder()
                .addQueryParameter("field", "name")
                //.addQueryParameter("limit", "2")
                .addQueryParameter("isStrict", "false")
                .addQueryParameter("token", "ZQQ8GMN-TN54SGK-NB3MKEC-ZKB8V06").build()

            val request = it.request().newBuilder()
                .url(url)
                .build()

            it.proceed(request)
        }

        //val loggingInterceptor = HttpLoggingInterceptor()
        //loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val httpClient = OkHttpClient.Builder()
            //.addInterceptor(loggingInterceptor)
            .addInterceptor(queryInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.kinopoisk.dev/")
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        searchService = retrofit.create(KinopoiskAPI::class.java)
    }
}