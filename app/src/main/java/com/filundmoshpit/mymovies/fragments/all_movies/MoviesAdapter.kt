package com.filundmoshpit.mymovies.fragments.all_movies

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.filundmoshpit.mymovies.R
import com.filundmoshpit.mymovies.data.Movie

class MoviesAdapter : ListAdapter<Movie, MoviesAdapter.MoviesViewHolder>(MovieDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)

        //val binding = MovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MoviesViewHolder(view)
    }

    override fun onCurrentListChanged(previousList: MutableList<Movie>, currentList: MutableList<Movie>) {
        count = currentList.size
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.movie_item
    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemCount(): Int {
        return count
    }

    inner class MoviesViewHolder(private val item: View) : RecyclerView.ViewHolder(item) {

        private var name: TextView = itemView.findViewById(R.id.movie_name)
        private var description: TextView = itemView.findViewById(R.id.movie_description)
        private var poster: ImageView = itemView.findViewById(R.id.movie_poster)

//        private var movie: Movie? = null

        fun bind(movie: Movie) {
//            this.movie = movie
            this.name.text = movie.name
            this.description.text = movie.description

            Glide.with(item)
                .load(movie.image)
                .into(poster)

            Log.d("MOVIE", "ID ${movie.id} : ${movie.image}")

            //setName(movie.name)
            //setDescription(movie.description)
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
        return oldItem.id == newItem.id
                && oldItem.name == newItem.name
                && oldItem.description == newItem.description
                //&& oldItem.image == newItem.image
    }
}