package com.hmn.data.domain

import com.hmn.data.model.resp.ApiResponse
import com.hmn.data.model.resp.AppDataResult
import com.hmn.data.model.resp.MovieListResponse
import com.hmn.data.model.resp.MovieVo
import com.hmn.data.utils.UrlConstants
import retrofit2.Response
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieRepo {
    suspend fun getAndSaveNowPlayingMovies(): AppDataResult<String>

    suspend fun getAndSaveTopRatedMovies(): AppDataResult<String>

    suspend fun getAndSavePopularMovies(): AppDataResult<String>

    suspend fun getMovieDetails(movieId: String): ApiResponse<MovieVo>
}