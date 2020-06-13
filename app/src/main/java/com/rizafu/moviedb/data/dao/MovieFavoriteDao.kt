package com.rizafu.moviedb.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rizafu.moviedb.data.entity.MovieFavoriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieFavoriteDao {
    @Query("SELECT * FROM MovieFavoriteEntity")
    fun getAll(): Flow<List<MovieFavoriteEntity>>

    @Query("SELECT * FROM MovieFavoriteEntity WHERE id LIKE :movieId")
    fun findById(movieId: Int): Flow<MovieFavoriteEntity?>

    @Query("DELETE FROM MovieFavoriteEntity WHERE id LIKE :movieId")
    fun deleteById(movieId: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg movieOverviewEntity: MovieFavoriteEntity)
}