package com.filundmoshpit.mymovies.presentation.watch_later

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.filundmoshpit.mymovies.R
import com.filundmoshpit.mymovies.domain.Movie
import com.filundmoshpit.mymovies.presentation.search.SearchViewModel

class WatchLaterListAdapter(val viewModel: WatchLaterViewModel) : ListAdapter<Movie, WatchLaterListAdapter.MoviesViewHolder>(MovieDiffCallback) {

    companion object {
        var count = 0
    }

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

        private val favourite: ImageView = itemView.findViewById(R.id.movie_favourite)
        private val watchLater: ImageView = itemView.findViewById(R.id.movie_watch_later)

        private var movie: Movie? = null

        init {
            favourite.setOnClickListener {
                if (movie != null) {
                    val currentMovie = movie!!

                    //currentMovie.setFavourite(true)
                    currentMovie.changeFavourite()

                    viewModel.updateFavourite(currentMovie)

                    setIcons()

                    //Toast.makeText(it.context, "Favourite: ${currentMovie.getName()}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        fun bind(movie: Movie) {
            this.movie = movie
            this.name.text = movie.getName()
            this.description.text = movie.getDescription()

            Glide.with(item)
                .load(movie.getImage())
                .into(poster)

            setIcons()
        }

        private fun setIcons() {
            //Watch later icon are always invisible on this ViewHolder
            watchLater.visibility = View.INVISIBLE

            if (movie?.getFavourite() == true) {
                favourite.setImageDrawable(AppCompatResources.getDrawable(item.context, R.drawable.ic_list_item_favourite_checked))
            }
            else {
                favourite.setImageDrawable(AppCompatResources.getDrawable(item.context, R.drawable.ic_list_item_favourite_unchecked))
            }
        }
    }

    object MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.getID() == newItem.getID()
                    && oldItem.getName() == newItem.getName()
                    && oldItem.getDescription() == newItem.getDescription()
                    && oldItem.getImage() == newItem.getImage()
        }
    }
}