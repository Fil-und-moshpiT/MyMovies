package com.filundmoshpit.mymovies.presentation

import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.filundmoshpit.mymovies.NavBottomFragmentsDirections
import com.filundmoshpit.mymovies.databinding.MovieListItemBinding
import com.filundmoshpit.mymovies.domain.MovieEntity

class ListItemViewHolder(private val itemBinding: MovieListItemBinding) :
    RecyclerView.ViewHolder(itemBinding.root) {

    private var movie: MovieEntity? = null

    init {
        itemView.setOnClickListener {
            if (movie != null) {
//                val movieCardTransitionName = itemView.context.getString(R.string.movie_card_transition_name)
                val action = NavBottomFragmentsDirections.actionNavBottomFragmentsNavMovieCardFragment(movie!!.getID())
//                val extras = FragmentNavigatorExtras(itemView to movieCardTransitionName)

//                itemView.findNavController().navigate(action, extras)
                itemView.findNavController().navigate(action)
            }
        }
    }

    fun bind(movie: MovieEntity) {
        itemView.transitionName = "movie_list_item_${movie.getID()}"

        this.movie = movie
        itemBinding.movieTitle.text = movie.getName()
        itemBinding.movieDescription.text = movie.getDescription()

        Glide.with(itemView)
            .load(movie.getImage())
            .into(itemBinding.moviePoster)
    }
}