package com.filundmoshpit.mymovies.presentation.favourites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.filundmoshpit.mymovies.domain.usecases.FavouritesUseCase

class FavouritesViewModelFactory(private val useCase: FavouritesUseCase) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FavouritesViewModel(useCase) as T
    }
}