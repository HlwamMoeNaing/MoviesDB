package com.hmn.data.di

import com.hmn.data.domain.MovieRepo
import com.hmn.data.domain.MoviesDbRepo
import com.hmn.data.movies_data.local.AppDatabase
import com.hmn.data.movies_data.network.TheMoviesDbApiService
import com.hmn.data.repo.MovieRepoImpl
import com.hmn.data.repo.MoviesDbRepoImpl
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
    fun providesMovieRepository(
        apiService: TheMoviesDbApiService,
       moviesDbRepo: MoviesDbRepo
    ): MovieRepo {
        return MovieRepoImpl(apiService = apiService,moviesDbRepo)
    }

    @Provides
    @Singleton
    fun providesMovieDbRepository(
        appDatabase: AppDatabase
    ): MoviesDbRepo {
        return MoviesDbRepoImpl(appDatabase)
    }
}