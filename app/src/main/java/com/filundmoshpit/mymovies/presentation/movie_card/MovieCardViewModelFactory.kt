package com.filundmoshpit.mymovies.presentation.movie_card

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.filundmoshpit.mymovies.domain.MovieCardUseCase

class MovieCardViewModelFactory(private val useCase: MovieCardUseCase) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MovieCardViewModel(useCase) as T
    }
}