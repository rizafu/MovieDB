package com.rizafu.moviedb.di.module

import android.app.Application
import androidx.room.Room
import com.rizafu.moviedb.data.AppDatabase
import com.rizafu.moviedb.data.dao.MovieFavoriteDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DbModule {

    @Singleton
    @Provides
    fun provideDataBase(app: Application): AppDatabase {
        return Room.databaseBuilder(
            app,
            AppDatabase::class.java, "database-movie"
        ).fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun provideMovieFavoriteDao(appDatabase: AppDatabase): MovieFavoriteDao {
        return appDatabase.movieFavoriteDao()
    }
}