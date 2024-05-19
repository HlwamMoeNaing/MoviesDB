package com.hmn.data.movies_data.network

import com.hmn.data.model.resp.MovieListResponse
import com.hmn.data.model.resp.MovieVo
import com.hmn.data.model.resp.Video
import com.hmn.data.utils.UrlConstants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TheMoviesDbApiService {
    @GET(UrlConstants.API_GET_NOW_PLAYING)
    suspend fun getNowPlayingMovies(
        @Query(UrlConstants.PARAM_API_KEY) apiKey: String = UrlConstants.MOVIE_API_KEY,
        @Query(UrlConstants.PARAM_PAGE) page: Int = 1
    ): Response<MovieListResponse>


    @GET(UrlConstants.API_TOP_RATE)
    suspend fun getTopRatedMovies(
        @Query(UrlConstants.PARAM_API_KEY) apiKey: String = UrlConstants.MOVIE_API_KEY,
        @Query(UrlConstants.PARAM_PAGE) page: Int = 1
    ): Response<MovieListResponse>

    @GET(UrlConstants.API_POPULAR)
    suspend fun getPopularMovies(
        @Query(UrlConstants.PARAM_API_KEY) apiKey: String = UrlConstants.MOVIE_API_KEY,
        @Query(UrlConstants.PARAM_PAGE) page: Int = 1
        ): Response<MovieListResponse>


    @GET("${UrlConstants.API_GET_MOVIE_DETAIL}/{movie_id}")
    suspend fun getMovieDetailsWithFlow(
        @Path("movie_id") movieId:String,
        @Query(UrlConstants.PARAM_API_KEY) apiKey: String = UrlConstants.MOVIE_API_KEY,
        @Query(UrlConstants.PARAM_PAGE) page: Int = 1
    ):Response<MovieVo>


    @GET("movie/{movie_id}/videos")
    suspend fun getMovieVideos(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = UrlConstants.MOVIE_API_KEY
    ): Response<Video>
}