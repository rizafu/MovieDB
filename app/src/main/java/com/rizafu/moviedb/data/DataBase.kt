package com.rizafu.moviedb.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rizafu.moviedb.data.dao.MovieFavoriteDao
import com.rizafu.moviedb.data.entity.MovieFavoriteEntity

@Database(entities = [MovieFavoriteEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieFavoriteDao(): MovieFavoriteDao
}