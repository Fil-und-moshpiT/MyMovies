package com.filundmoshpit.mymovies.presentation.favourites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.filundmoshpit.mymovies.R
import com.filundmoshpit.mymovies.databinding.MovieListItemBinding
import com.filundmoshpit.mymovies.domain.MovieEntity

class FavouritesListAdapter : ListAdapter<MovieEntity, FavouritesListAdapter.MoviesViewHolder>(MovieDiffCallback) {

    companion object {
        var count = 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        val binding = MovieListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MoviesViewHolder(binding)
    }

    override fun onCurrentListChanged(previousList: MutableList<MovieEntity>, currentList: MutableList<MovieEntity>) {
        count = currentList.size
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.movie_list_item
    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemCount(): Int {
        return count
    }

    inner class MoviesViewHolder(private val itemBinding: MovieListItemBinding) : RecyclerView.ViewHolder(itemBinding.root) {

        private var movie: MovieEntity? = null

        init {
            itemView.setOnClickListener {
                if (movie != null) {
                    val movieCardTransitionName = itemView.context.getString(R.string.movie_card_transition_name)

                    val action = FavouritesFragmentDirections.actionNavBottomFragmentsNavMovieCardFragment(movie!!.getID())
                    val extras = FragmentNavigatorExtras(itemView to movieCardTransitionName)

                    itemView.findNavController().navigate(action, extras)
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

    object MovieDiffCallback : DiffUtil.ItemCallback<MovieEntity>() {
        override fun areItemsTheSame(oldItem: MovieEntity, newItem: MovieEntity): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: MovieEntity, newItem: MovieEntity): Boolean {
            return oldItem.getID() == newItem.getID()
                    && oldItem.getName() == newItem.getName()
                    && oldItem.getDescription() == newItem.getDescription()
                    && oldItem.getImage() == newItem.getImage()
        }
    }
}