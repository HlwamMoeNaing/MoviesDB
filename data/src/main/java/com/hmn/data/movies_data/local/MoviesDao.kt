package com.hmn.data.movies_data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hmn.data.model.resp.MovieVo
import kotlinx.coroutines.flow.Flow


@Dao
interface MoviesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<MovieVo>)

    @Query("SELECT * FROM movies")
    fun getAllMovies(): Flow<List<MovieVo>>

    @Query("DELETE FROM movies")
    suspend fun deleteAllSaveMovies()


    @Query("SELECT * From movies Where category LIKE :mCategory")
    suspend fun getMoviesWithCategory(mCategory: String): List<MovieVo>


    @Query("SELECT * From movies Where id = :id")
    suspend fun getMovieById(id: Int): MovieVo

    @Update
    fun updateMovie(entity: MovieVo)

    @Query("SELECT * FROM movies WHERE title LIKE '%' || :title || '%'")
    fun searchMoviesByName(title:String): Flow<List<MovieVo>>

}