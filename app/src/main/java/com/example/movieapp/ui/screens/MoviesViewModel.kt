package com.example.movieapp.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.data.local.MovieEntity
import com.example.movieapp.data.repository.MovieRepository
import com.example.movieapp.ui.components.toEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MoviesUiState(
    val isLoading: Boolean = false,
    val movies: List<MovieEntity> = emptyList(),
    val error: String? = null,
    val searchQuery: String = ""
)

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MoviesUiState())
    val uiState: StateFlow<MoviesUiState> = _uiState.asStateFlow()

    // Cache to hold raw API results
    private val rawApiMovies = MutableStateFlow<List<com.example.movieapp.data.model.MovieDto>>(emptyList())

    // Observe local data
    private val favourites = repository.getFavourites()
    private val watchlist = repository.getWatchlist()

    init {
        loadPopularMovies()
        
        // merge API results with local state
        viewModelScope.launch {
            combine(rawApiMovies, favourites, watchlist) { apiList, favs, watch ->
                val favIds = favs.map { it.id }.toSet()
                val watchIds = watch.map { it.id }.toSet()
                
                apiList.map { dto ->
                    dto.toEntity(
                        isFav = favIds.contains(dto.id),
                        isWatch = watchIds.contains(dto.id)
                    )
                }
            }.collect { mergedList ->
                _uiState.update { it.copy(movies = mergedList) }
            }
        }
    }

    fun loadPopularMovies() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            val result = repository.getPopularMovies()
            result.onSuccess { list ->
                rawApiMovies.value = list
                _uiState.update { it.copy(isLoading = false) }
            }.onFailure { e ->
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun searchMovies(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        if (query.isBlank()) {
            loadPopularMovies()
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            val result = repository.searchMovies(query)
            result.onSuccess { list ->
                 rawApiMovies.value = list
                _uiState.update { it.copy(isLoading = false) }
            }.onFailure { e ->
                 _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

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
