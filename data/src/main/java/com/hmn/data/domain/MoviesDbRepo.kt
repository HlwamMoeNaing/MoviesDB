package com.hmn.data.domain

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hmn.data.model.resp.MovieVo
import kotlinx.coroutines.flow.Flow

interface MoviesDbRepo {
    suspend fun insertMovies(movies: List<MovieVo>)
    suspend fun getAllMovies(): Flow<List<MovieVo>>
    suspend fun deleteAllSaveMovies()
    suspend fun getMoviesWithCategory(mCategory: String): List<MovieVo>
}