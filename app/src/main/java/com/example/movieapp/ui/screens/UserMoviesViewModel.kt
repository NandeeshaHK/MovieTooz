package com.example.movieapp.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.data.local.MovieEntity
import com.example.movieapp.data.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserMoviesViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    val favourites: StateFlow<List<MovieEntity>> = repository.getFavourites()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val watchlist: StateFlow<List<MovieEntity>> = repository.getWatchlist()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun toggleFavourite(movie: MovieEntity) {
        viewModelScope.launch {
            repository.toggleFavourite(movie)
        }
    }

    fun toggleWatchlist(movie: MovieEntity) {
        viewModelScope.launch {
            repository.toggleWatchlist(movie)
        }
    }
}
