package com.example.movieapp.ui.screens

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.data.local.MovieEntity
import com.example.movieapp.data.repository.MovieRepository
import com.example.movieapp.ui.components.toEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MovieDetailsUiState(
    val isLoading: Boolean = false,
    val movie: MovieEntity? = null,
    val error: String? = null
)

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val repository: MovieRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val movieId: String? = savedStateHandle["movieId"]
    
    private val _uiState = MutableStateFlow(MovieDetailsUiState())
    val uiState: StateFlow<MovieDetailsUiState> = _uiState.asStateFlow()

    init {
        movieId?.toIntOrNull()?.let { loadGameDetails(it) }
    }

    private fun loadGameDetails(id: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            // Check if it is in DB first to get fav status immediately?
            // But we want fresh details.
            // We should fetch from API, and then check DB status.
            
            val apiResult = repository.getMovieDetails(id)
            if (apiResult.isSuccess) {
                val dto = apiResult.getOrThrow()
                val isFav = repository.isFavourite(id)
                val isWatch = repository.isWatchlist(id)
                val entity = dto.toEntity(isFav, isWatch)
                _uiState.update { it.copy(isLoading = false, movie = entity) }
            } else {
                // If API fails, try to get from DB if it exists?
                val dbEntity = repository.getFavourites() // accessing flow... hard to get single value synchronously
                // Simplified: Just use API error for now or basic DB check
                 _uiState.update { it.copy(isLoading = false, error = apiResult.exceptionOrNull()?.message) }
            }
        }
    }
    
    fun toggleFavourite() {
        val current = _uiState.value.movie ?: return
        viewModelScope.launch {
             repository.toggleFavourite(current)
             // Update local state to reflect change immediately in UI
             _uiState.update { state -> 
                 state.copy(movie = state.movie?.copy(isFavourite = !current.isFavourite))
             }
        }
    }
    
     fun toggleWatchlist() {
        val current = _uiState.value.movie ?: return
        viewModelScope.launch {
             repository.toggleWatchlist(current)
             _uiState.update { state -> 
                 state.copy(movie = state.movie?.copy(isWatchlist = !current.isWatchlist))
             }
        }
    }
}
