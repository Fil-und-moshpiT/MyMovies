package com.filundmoshpit.mymovies.fragments.all_movies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.filundmoshpit.mymovies.R
import com.filundmoshpit.mymovies.data.Movie
import com.filundmoshpit.mymovies.databinding.MovieItemBinding

class MoviesAdapter : ListAdapter<Movie, MoviesAdapter.MoviesViewHolder>(MovieDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)

        //val binding = MovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        val result = MoviesViewHolder(view)
        result.setName(count.toString())

        return result
    }

    override fun onCurrentListChanged(previousList: MutableList<Movie>, currentList: MutableList<Movie>) {
        count = currentList.size
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.movie_item
    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        holder.bind(getItem(position))
        //holder.setName(position.toString())
    }

    override fun getItemCount(): Int {
        return count
    }

    inner class MoviesViewHolder(item: View) : RecyclerView.ViewHolder(item) {

        private var name: TextView = itemView.findViewById(R.id.movie_name)
        private var description: TextView = itemView.findViewById(R.id.movie_description)

        private var movie: Movie? = null

        fun setName(value: String) {
            name.text = value
        }

        fun setDescription(value: String) {
            description.text = value
        }

        fun bind(movie: Movie) {
            this.movie = movie

            setName(movie.name)
            setDescription(movie.description)
        }
    }

    companion object {
        var count = 0
    }
}

object MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.name == newItem.name
                && oldItem.description == newItem.description
                && oldItem.image == newItem.image
    }
}