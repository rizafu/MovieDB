package com.rizafu.moviedb.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MovieFavoriteEntity(
    @PrimaryKey val id: Int = -1,
    val adult: Boolean = true,
    val overview: String = "",
    val popularity: Double = 0.0,
    val title: String = "",
    val video: Boolean = false,
    @ColumnInfo(name = "backdrop_path") val backdropPath: String? = null,
    @ColumnInfo(name = "original_language") val originalLanguage: String = "",
    @ColumnInfo(name = "original_title") val originalTitle: String = "",
    @ColumnInfo(name = "poster_path") val posterPath: String? = null,
    @ColumnInfo(name = "release_date") val releaseDate: String = "",
    @ColumnInfo(name = "vote_average") val voteAverage: Double = 0.0,
    @ColumnInfo(name = "vote_count") val voteCount: Int = 0
)