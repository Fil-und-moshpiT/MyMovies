package com.filundmoshpit.mymovies.presentation.favourites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.filundmoshpit.mymovies.R
import com.filundmoshpit.mymovies.domain.MovieEntity

class FavouritesListAdapter : ListAdapter<MovieEntity, FavouritesListAdapter.MoviesViewHolder>(MovieDiffCallback) {

    companion object {
        var count = 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)

        //val binding = MovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MoviesViewHolder(view)
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

    inner class MoviesViewHolder(item: View) : RecyclerView.ViewHolder(item) {

        private var name: TextView = itemView.findViewById(R.id.movie_name)
        private var description: TextView = itemView.findViewById(R.id.movie_description)
        private var poster: ImageView = itemView.findViewById(R.id.movie_poster)

        private var movie: MovieEntity? = null

        init {
            itemView.setOnClickListener {
                if (movie != null) {
                    val bundle = Bundle().apply {
                        putInt("id", movie!!.getID())
                    }

                    itemView.findNavController().navigate(R.id.action_nav_bottom_fragments_nav_movie_card_fragment, bundle)
                }
            }
        }

        fun bind(movie: MovieEntity) {
            this.movie = movie
            this.name.text = movie.getName()
            this.description.text = movie.getDescription()

            Glide.with(itemView)
                .load(movie.getImage())
                .into(poster)
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