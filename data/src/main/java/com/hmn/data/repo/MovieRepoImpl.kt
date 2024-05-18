package com.hmn.data.repo

import com.hmn.data.domain.MovieRepo
import com.hmn.data.domain.MoviesDbRepo
import com.hmn.data.model.resp.ApiResponse
import com.hmn.data.model.resp.AppDataResult
import com.hmn.data.model.resp.MovieListResponse
import com.hmn.data.model.resp.MovieVo
import com.hmn.data.movies_data.local.AppDatabase
import com.hmn.data.movies_data.network.ApiResponseHandler
import com.hmn.data.movies_data.network.TheMoviesDbApiService
import com.hmn.data.utils.MovieCategory
import retrofit2.Response
import javax.inject.Inject

class MovieRepoImpl @Inject constructor(
    private val apiService: TheMoviesDbApiService,
    private val dbRepoImpl: MoviesDbRepo
) : MovieRepo {
    override suspend fun getAndSaveNowPlayingMovies(): AppDataResult<String> {
        return try {
            when (val response = getNowPlayingMovies()) {
                is ApiResponse.ApiError -> {
                    val errorMessage = response.message
                    AppDataResult.Error(Exception(errorMessage))
                }

                is ApiResponse.AuthorizationError -> {
                    val errorMessage = response.message
                    AppDataResult.Error(Exception(errorMessage))
                }

                ApiResponse.Default -> {
                    AppDataResult.Default
                }

                ApiResponse.Loading -> {
                    AppDataResult.Loading
                }

                is ApiResponse.NetworkError -> {
                    val errorMessage = response.message
                    AppDataResult.Error(Exception(errorMessage))
                }

                is ApiResponse.ServerConnectionError -> {
                    val errorMessage = response.message
                    AppDataResult.Error(Exception(errorMessage))
                }

                is ApiResponse.Success -> {
                    val result = response.data
                    var movies = result?.results ?: emptyList()
                    movies = movies.map { movie ->
                        movie.copy(category = MovieCategory.NOW_PLAYING.name)
                    }
                    dbRepoImpl.deleteAllSaveMovies()
                    dbRepoImpl.insertMovies(movies)
                    AppDataResult.Success("Success")
                }

                is ApiResponse.TimeoutError -> {
                    val errorMessage = response.message
                    AppDataResult.Error(Exception(errorMessage))
                }
            }
        } catch (e: Exception) {
            AppDataResult.Error(e)
        }
    }

    override suspend fun getAndSaveTopRatedMovies(): AppDataResult<String> {
        return try {
            when (val response = getTopRateMovies()) {
                is ApiResponse.ApiError -> {
                    val errorMessage = response.message
                    AppDataResult.Error(Exception(errorMessage))
                }

                is ApiResponse.AuthorizationError -> {
                    val errorMessage = response.message
                    AppDataResult.Error(Exception(errorMessage))
                }

                ApiResponse.Default -> {
                    AppDataResult.Default
                }

                ApiResponse.Loading -> {
                    AppDataResult.Loading
                }

                is ApiResponse.NetworkError -> {
                    val errorMessage = response.message
                    AppDataResult.Error(Exception(errorMessage))
                }

                is ApiResponse.ServerConnectionError -> {
                    val errorMessage = response.message
                    AppDataResult.Error(Exception(errorMessage))
                }

                is ApiResponse.Success -> {
                    val result = response.data
                    var movies = result?.results ?: emptyList()
                    movies = movies.map { movie ->
                        movie.copy(category = MovieCategory.TOP_RATED.name)
                    }
                    dbRepoImpl.deleteAllSaveMovies()
                    dbRepoImpl.insertMovies(movies)
                    AppDataResult.Success("Success")
                }

                is ApiResponse.TimeoutError -> {
                    val errorMessage = response.message
                    AppDataResult.Error(Exception(errorMessage))
                }
            }
        } catch (e: Exception) {
            AppDataResult.Error(e)
        }
    }

    override suspend fun getAndSavePopularMovies(): AppDataResult<String> {
        return try {
            when (val response = getPopularMovies()) {
                is ApiResponse.ApiError -> {
                    val errorMessage = response.message
                    AppDataResult.Error(Exception(errorMessage))
                }

                is ApiResponse.AuthorizationError -> {
                    val errorMessage = response.message
                    AppDataResult.Error(Exception(errorMessage))
                }

                ApiResponse.Default -> {
                    AppDataResult.Default
                }

                ApiResponse.Loading -> {
                    AppDataResult.Loading
                }

                is ApiResponse.NetworkError -> {
                    val errorMessage = response.message
                    AppDataResult.Error(Exception(errorMessage))
                }

                is ApiResponse.ServerConnectionError -> {
                    val errorMessage = response.message
                    AppDataResult.Error(Exception(errorMessage))
                }

                is ApiResponse.Success -> {
                    val result = response.data
                    var movies = result?.results ?: emptyList()
                    movies = movies.map { movie ->
                        movie.copy(category = MovieCategory.POPULAR.name)
                    }
                    dbRepoImpl.deleteAllSaveMovies()
                    dbRepoImpl.insertMovies(movies)
                    AppDataResult.Success("Success")
                }

                is ApiResponse.TimeoutError -> {
                    val errorMessage = response.message
                    AppDataResult.Error(Exception(errorMessage))
                }
            }
        } catch (e: Exception) {
            AppDataResult.Error(e)
        }
    }

    override suspend fun getMovieDetails(movieId: String): ApiResponse<MovieVo> {
        return ApiResponseHandler.processResponse {
            apiService.getMovieDetailsWithFlow(movieId)
        }
    }


    private suspend fun getNowPlayingMovies(): ApiResponse<MovieListResponse> {
        return ApiResponseHandler.processResponse {
            apiService.getNowPlayingMovies()
        }
    }

    private suspend fun getTopRateMovies(): ApiResponse<MovieListResponse> {
        return ApiResponseHandler.processResponse {
            apiService.getTopRatedMovies()
        }
    }

    private suspend fun getPopularMovies(): ApiResponse<MovieListResponse> {
        return ApiResponseHandler.processResponse {
            apiService.getTopRatedMovies()
        }
    }


}