package com.rizafu.moviedb.di.module

import com.rizafu.moviedb.data.ApiService
import com.rizafu.moviedb.data.Repository
import com.rizafu.moviedb.data.RepositoryImpl
import com.rizafu.moviedb.data.dao.MovieFavoriteDao
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Module
class RepositoryModule {

    @Singleton
    @Provides
    fun providesRepository(apiService: ApiService, movieFavoriteDao: MovieFavoriteDao): Repository {
        return RepositoryImpl(apiService, movieFavoriteDao)
    }
}