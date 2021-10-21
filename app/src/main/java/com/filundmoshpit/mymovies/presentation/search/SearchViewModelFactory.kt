package com.filundmoshpit.mymovies.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.filundmoshpit.mymovies.domain.SearchUseCase

class SearchViewModelFactory(private val useCase: SearchUseCase) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SearchViewModel(useCase) as T
    }
}