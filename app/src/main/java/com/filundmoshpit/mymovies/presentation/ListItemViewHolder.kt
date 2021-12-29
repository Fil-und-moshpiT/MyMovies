package com.filundmoshpit.mymovies.presentation

import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.filundmoshpit.mymovies.databinding.MovieListItemBinding
import com.filundmoshpit.mymovies.domain.MovieEntity
import com.filundmoshpit.mymovies.presentation.movie_card.MovieCardActivity

class ListItemViewHolder(private val itemBinding: MovieListItemBinding) :
    RecyclerView.ViewHolder(itemBinding.root) {

    private var movie: MovieEntity? = null

    init {
        itemView.setOnClickListener {
            if (movie != null) {
                ContextCompat.startActivity(
                    itemView.context,
                    Intent(itemView.context, MovieCardActivity::class.java).apply {
                        putExtra("movieId", movie!!.id)
                    },
                    null
                )
            }
        }
    }

    fun bind(movie: MovieEntity) {
        itemView.transitionName = "movie_list_item_${movie.id}"

        this.movie = movie
        itemBinding.movieTitle.text = movie.name
        itemBinding.movieDescription.text = movie.description

        Glide.with(itemView)
            .load(movie.imageSmall)
            .into(itemBinding.moviePoster)
    }
}