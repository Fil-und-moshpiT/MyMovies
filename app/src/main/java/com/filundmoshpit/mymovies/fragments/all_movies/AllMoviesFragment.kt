package com.filundmoshpit.mymovies.fragments.all_movies

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.filundmoshpit.mymovies.R
import com.filundmoshpit.mymovies.data.Movie

class AllMoviesFragment : Fragment() {

    private var moviesViewModel = MoviesViewModel()

    private lateinit var viewSearchText: EditText
    private lateinit var viewSearchButton: Button
    private lateinit var viewAllMoviesList: RecyclerView

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_all_movies, container, false)

        val moviesAdapter = MoviesAdapter()

        viewSearchText = root.findViewById(R.id.et_search)
        viewSearchButton = root.findViewById(R.id.b_search)
        viewSearchButton.setOnClickListener {
            moviesViewModel.add(Movie(viewSearchText.text.toString(), "", ""))
        }

        viewAllMoviesList = root.findViewById(R.id.rv_list)
        viewAllMoviesList.setHasFixedSize(true)
        viewAllMoviesList.layoutManager = LinearLayoutManager(root.context)
        viewAllMoviesList.adapter = moviesAdapter

        moviesViewModel.liveData.observe(viewLifecycleOwner, {
            it?.let {
                moviesAdapter.submitList(it as MutableList<Movie>)
            }
        })

        return root
    }

    companion object {
        @JvmStatic
        fun newInstance() = AllMoviesFragment()
    }
}