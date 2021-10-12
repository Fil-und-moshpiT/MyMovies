package com.filundmoshpit.mymovies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.commit
import com.filundmoshpit.mymovies.fragments.main.AllMoviesFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.commit {
            replace(R.id.f_all_movies, AllMoviesFragment.newInstance())
        }
    }
}