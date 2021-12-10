package com.filundmoshpit.mymovies.presentation.watch_later

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.filundmoshpit.mymovies.domain.usecases.WatchLaterUseCase

class WatchLaterViewModelFactory(private val useCase: WatchLaterUseCase) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return WatchLaterViewModel(useCase) as T
    }
}