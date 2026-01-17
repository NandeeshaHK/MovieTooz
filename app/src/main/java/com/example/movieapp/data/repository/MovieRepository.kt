package com.example.movieapp.data.repository

import com.example.movieapp.BuildConfig
import com.example.movieapp.data.local.MovieDao
import com.example.movieapp.data.local.MovieEntity
import com.example.movieapp.data.network.TmdbApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(
    private val api: TmdbApi,
    private val dao: MovieDao
) {

    suspend fun getPopularMovies(page: Int = 1): Result<List<com.example.movieapp.data.model.MovieDto>> {
        return try {
            val response = api.getPopularMovies(apiKey = BuildConfig.TMDB_API_KEY, page = page)
            Result.success(response.results)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun searchMovies(query: String, page: Int = 1): Result<List<com.example.movieapp.data.model.MovieDto>> {
         return try {
            val response = api.searchMovies(apiKey = BuildConfig.TMDB_API_KEY, query = query, page = page)
            Result.success(response.results)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getMovieDetails(movieId: Int): Result<com.example.movieapp.data.model.MovieDto> {
        return try {
            val response = api.getMovieDetails(movieId = movieId, apiKey = BuildConfig.TMDB_API_KEY)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getRecommendations(movieId: Int, page: Int = 1): Result<List<com.example.movieapp.data.model.MovieDto>> {
        return try {
            val response = api.getRecommendations(movieId = movieId, apiKey = BuildConfig.TMDB_API_KEY, page = page)
            Result.success(response.results)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getFavourites(): Flow<List<MovieEntity>> = dao.getFavourites()

    fun getWatchlist(): Flow<List<MovieEntity>> = dao.getWatchlist()

    suspend fun toggleFavourite(movie: MovieEntity) {
        val existing = dao.getMovieById(movie.id)
        if (existing != null) {
            val updated = existing.copy(isFavourite = !existing.isFavourite)
            // If neither favourite nor watchlist, maybe delete? For now, we update.
            // If both false, we delete to clean up DB
            if (!updated.isFavourite && !updated.isWatchlist) {
                dao.deleteMovie(movie.id)
            } else {
                dao.insertMovie(updated)
            }
        } else {
            // New entry, so it must be setting to true
            dao.insertMovie(movie.copy(isFavourite = true))
        }
    }

    suspend fun toggleWatchlist(movie: MovieEntity) {
         val existing = dao.getMovieById(movie.id)
         if (existing != null) {
            val updated = existing.copy(isWatchlist = !existing.isWatchlist)
             if (!updated.isFavourite && !updated.isWatchlist) {
                dao.deleteMovie(movie.id)
            } else {
                dao.insertMovie(updated)
            }
        } else {
             dao.insertMovie(movie.copy(isWatchlist = true))
        }
    }
    
    suspend fun isFavourite(id: Int): Boolean {
        return dao.getMovieById(id)?.isFavourite == true
    }
    
    suspend fun isWatchlist(id: Int): Boolean {
        return dao.getMovieById(id)?.isWatchlist == true
    }
}
