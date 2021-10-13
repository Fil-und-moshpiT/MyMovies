package com.filundmoshpit.mymovies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.filundmoshpit.mymovies.data.Movie
import com.filundmoshpit.mymovies.databinding.FragmentAllMoviesBinding
import com.filundmoshpit.mymovies.databinding.MovieItemBinding
import com.filundmoshpit.mymovies.fragments.all_movies.AllMoviesFragment
import com.filundmoshpit.mymovies.fragments.all_movies.MoviesAdapter
import com.filundmoshpit.mymovies.fragments.all_movies.MoviesViewModel

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Set all movies fragment as default
        supportFragmentManager.commit {
            replace(R.id.f_all_movies, AllMoviesFragment.newInstance())
        }
    }
}