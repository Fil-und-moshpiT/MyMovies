package com.filundmoshpit.mymovies.presentation.movie_card

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.filundmoshpit.mymovies.MainActivity
import com.filundmoshpit.mymovies.R

class MovieCardFragment : Fragment() {

    private lateinit var viewModel: MovieCardViewModel

    private lateinit var viewMoviePoster: ImageView
    private lateinit var viewMovieName: TextView
    private lateinit var viewMovieDescription: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_movie_card, container, false)

        viewModel =
            ViewModelProvider(requireActivity(), MovieCardViewModelFactory(MainActivity.movieCardUseCase))
                .get(MovieCardViewModel::class.java)

        viewMoviePoster = root.findViewById(R.id.movie_poster)
        viewMovieName = root.findViewById(R.id.movie_name)
        viewMovieDescription = root.findViewById(R.id.movie_description)

        return root
    }
}