package com.hmn.data.di

import com.hmn.data.domain.MoviesDbRepo
import com.hmn.data.movies_data.local.AppDatabase
import com.hmn.data.movies_data.network.TheMoviesDbApiService
import com.hmn.data.repo.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun providesTheMovieDbRepo(
        apiService: TheMoviesDbApiService,
        appDatabase: AppDatabase,
    ): MovieRepository {
        return MovieRepository(apiService,appDatabase)
    }
}