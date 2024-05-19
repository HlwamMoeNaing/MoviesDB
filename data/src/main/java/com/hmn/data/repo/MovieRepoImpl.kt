package com.hmn.data.repo

import com.hmn.data.model.ResourceState
import com.hmn.data.model.resp.MovieVo
import com.hmn.data.model.resp.Video
import com.hmn.data.movies_data.local.AppDatabase
import com.hmn.data.movies_data.network.TheMoviesDbApiService
import com.hmn.data.utils.MovieCategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val apiService: TheMoviesDbApiService,
    private val appDatabase: AppDatabase
) {
    suspend fun fetchAndSaveMovies(category: MovieCategory) {
        val apiResponse = when (category) {
            MovieCategory.POPULAR -> apiService.getPopularMovies()
            MovieCategory.NOW_PLAYING -> apiService.getNowPlayingMovies()
            MovieCategory.TOP_RATED -> apiService.getTopRatedMovies()
        }

        if (apiResponse.isSuccessful && apiResponse.body() != null) {
            val movies = apiResponse.body()?.results ?: emptyList()
            val moviesWithCategory = movies.map { it.copy(category = category.name) }
            appDatabase.movieDao().insertMovies(moviesWithCategory)
        } else {
            throw Exception(
                "Failed to fetch ${category.name} movies: ${
                    apiResponse.errorBody()?.string()
                }"
            )
        }
    }

    suspend fun getMoviesByCategory(category: String): List<MovieVo> {
        return appDatabase.movieDao().getMoviesWithCategory(category)
    }


    suspend fun checkMoviesExist(): Boolean {
        return withContext(Dispatchers.IO) {
            val count = appDatabase.movieDao().getMoviesCount()
            count != null && count > 0
        }
    }

    suspend fun getAllMovies(): Flow<List<MovieVo>> {
        return withContext(Dispatchers.IO) {
            appDatabase.movieDao().getAllMovies()
        }
    }

    suspend fun getMovieById(id: Int): MovieVo {
        return withContext(Dispatchers.IO) {
            appDatabase.movieDao().getMovieById(id)
        }
    }


    suspend fun searchMoviesByTitle(title: String): Flow<List<MovieVo>> {
        return withContext(Dispatchers.IO) {
            appDatabase.movieDao().searchMoviesByName(title)
        }
    }


    suspend fun updateFavStatus(movieId: Int, isFavourite: Boolean) {
        appDatabase.movieDao().updateFavouriteStatus(movieId, isFavourite)
    }

    suspend fun getFavouriteMovies(): List<MovieVo> {
        return withContext(Dispatchers.IO) {
            appDatabase.movieDao().getMoviesWhereFavourite()
        }
    }

    suspend fun deleteAllMovies() {
        withContext(Dispatchers.IO) {
            appDatabase.movieDao().deleteAllSaveMovies()
        }
    }

    suspend fun getVideoWithId(movieId: Int): ResourceState<Video> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getMovieVideos(movieId)
                if (response.isSuccessful && response.body() != null) {
                    ResourceState.Success(response.body()!!)
                } else {
                    val error = response.errorBody()?.string() ?: "Unknown error"
                    ResourceState.Error(error)
                }
            } catch (e: Exception) {
                ResourceState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }
}
