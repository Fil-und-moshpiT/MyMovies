package com.filundmoshpit.mymovies.presentation.movie_card

import androidx.lifecycle.ViewModel
import com.filundmoshpit.mymovies.domain.MovieCardUseCase
import com.filundmoshpit.mymovies.domain.MovieEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MovieCardViewModel(private val useCase: MovieCardUseCase) : ViewModel() {

    fun updateFavourite(movie: MovieEntity) {
        GlobalScope.launch(Dispatchers.IO) {
            useCase.updateFavourite(movie)
        }
    }

    fun updateWatchLater(movie: MovieEntity) {
        GlobalScope.launch(Dispatchers.IO) {
            useCase.updateWatchLater(movie)
        }
    }

}