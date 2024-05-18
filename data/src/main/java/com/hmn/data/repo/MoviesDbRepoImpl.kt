package com.hmn.data.repo

import com.hmn.data.domain.MoviesDbRepo
import com.hmn.data.model.resp.MovieVo
import com.hmn.data.movies_data.local.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MoviesDbRepoImpl @Inject constructor(
    private val appDatabase: AppDatabase
) : MoviesDbRepo {
    override suspend fun insertMovies(movies: List<MovieVo>) {
        withContext(Dispatchers.IO) {
            try {
                appDatabase.movieDao().insertMovies(movies)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override suspend fun getAllMovies(): Flow<List<MovieVo>> {
        return flow {
            try {
                val movies = withContext(Dispatchers.IO) {
                    appDatabase.movieDao().getAllMovies()
                        .first()
                }
                emit(movies)
            } catch (e: Exception) {
                emit(emptyList<MovieVo>())
            }
        }.flowOn(Dispatchers.IO)
    }


    override suspend fun deleteAllSaveMovies() {
        withContext(Dispatchers.IO) {
            try {
                appDatabase.movieDao().deleteAllSaveMovies()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override suspend fun getMoviesWithCategory(mCategory: String): List<MovieVo> {
        return withContext(Dispatchers.IO) {
            try {
                appDatabase.movieDao().getMoviesWithCategory(mCategory)
            } catch (e: Exception) {
                emptyList<MovieVo>()
            }
        }
    }
}