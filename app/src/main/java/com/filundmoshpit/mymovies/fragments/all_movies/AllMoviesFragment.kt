package com.filundmoshpit.mymovies.fragments.all_movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.filundmoshpit.mymovies.MainActivity
import com.filundmoshpit.mymovies.R
import com.filundmoshpit.mymovies.data.Movie
import com.filundmoshpit.mymovies.data.SearchResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllMoviesFragment : Fragment() {

    private var moviesViewModel = MoviesViewModel()

    private lateinit var viewSearchText: EditText
    private lateinit var viewSearchButton: Button
    private lateinit var viewAllMoviesList: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val root = inflater.inflate(R.layout.fragment_all_movies, container, false)

        viewSearchText = root.findViewById(R.id.et_search)
        viewSearchButton = root.findViewById(R.id.b_search)
        viewSearchButton.setOnClickListener {
            search()
        }

        val moviesAdapter = MoviesAdapter()

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

    private fun search() {
        val name = viewSearchText.text.toString()

        GlobalScope.launch(Dispatchers.IO) {
            MainActivity.searchService.search(name).enqueue(
                object : Callback<SearchResponse> {
                    override fun onResponse(call: Call<SearchResponse>, response: Response<SearchResponse>) {
                        val searchResponse = response.body()?.docs

                        if (searchResponse != null) {
                            moviesViewModel.replace(searchResponse)
                        }
                    }

                    override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                        moviesViewModel.clear()
                    }
                },
            )
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = AllMoviesFragment()
    }
}