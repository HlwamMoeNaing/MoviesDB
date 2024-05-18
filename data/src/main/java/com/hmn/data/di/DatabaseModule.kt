package com.hmn.data.di

import android.app.Application
import com.hmn.data.movies_data.local.AppDatabase
import com.hmn.data.movies_data.local.MyDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {


    @Provides
    @Singleton
    fun provideDatabase(application: Application) = AppDatabase.getInstance(application.applicationContext)

    @Provides
    @Singleton
    fun provideMyDataStore(application: Application): MyDataStore = MyDataStore(application)
}