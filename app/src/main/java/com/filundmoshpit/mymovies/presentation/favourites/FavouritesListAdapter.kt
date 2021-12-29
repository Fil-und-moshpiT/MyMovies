package com.filundmoshpit.mymovies.presentation.favourites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.filundmoshpit.mymovies.R
import com.filundmoshpit.mymovies.databinding.MovieListItemBinding
import com.filundmoshpit.mymovies.domain.MovieEntity
import com.filundmoshpit.mymovies.presentation.ListItemViewHolder

class FavouritesListAdapter : ListAdapter<MovieEntity, ListItemViewHolder>(MovieDiffCallback) {

    private lateinit var binding: MovieListItemBinding

    companion object {
        var count = 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        binding = MovieListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ListItemViewHolder(binding)
    }

    override fun onCurrentListChanged(previousList: MutableList<MovieEntity>, currentList: MutableList<MovieEntity>) {
        count = currentList.size
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.movie_list_item
    }

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemCount(): Int {
        return count
    }

    object MovieDiffCallback : DiffUtil.ItemCallback<MovieEntity>() {
        override fun areItemsTheSame(oldItem: MovieEntity, newItem: MovieEntity): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: MovieEntity, newItem: MovieEntity): Boolean {
            return oldItem.id == newItem.id
                    && oldItem.name == newItem.name
                    && oldItem.description == newItem.description
                    && oldItem.imageSmall == newItem.imageSmall
                    && oldItem.imageBig == newItem.imageBig
        }
    }
}