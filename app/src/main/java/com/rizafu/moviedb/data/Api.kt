package com.rizafu.moviedb.data

import com.rizafu.moviedb.data.model.MovieModel
import com.rizafu.moviedb.data.model.MovieOverviewModel
import com.rizafu.moviedb.data.model.MovieReviewModel
import com.rizafu.moviedb.data.model.PageResultModel
import com.squareup.moshi.JsonAdapter
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import se.ansman.kotshi.KotshiJsonAdapterFactory

interface ApiService {

    @GET("movie/popular")
    suspend fun getPopular(
        @Query("page") page: Int = 1,
        @Query("language") language: String = "en-US"
    ): PageResultModel<MovieOverviewModel>

    @GET("movie/top_rated")
    suspend fun getTopRated(
        @Query("page") page: Int = 1,
        @Query("language") language: String = "en-US"
    ): PageResultModel<MovieOverviewModel>

    @GET("movie/now_playing")
    suspend fun getNowPlaying(
        @Query("page") page: Int = 1,
        @Query("language") language: String = "en-US"
    ): PageResultModel<MovieOverviewModel>

    @GET("movie/upcoming")
    suspend fun getUpcoming(
        @Query("page") page: Int = 1,
        @Query("language") language: String = "en-US"
    ): PageResultModel<MovieOverviewModel>

    @GET("movie/{movie_id}")
    suspend fun getMovieDetail(
        @Path("movie_id") movieId: Int,
        @Query("language") language: String = "en-US"
    ): MovieModel

    @GET("movie/{movie_id}/reviews")
    suspend fun getMovieReviews(
        @Path("movie_id") movieId: Int,
        @Query("page") page: Int = 1,
        @Query("language") language: String = "en-US"
    ): PageResultModel<MovieReviewModel>
}

@KotshiJsonAdapterFactory
object ApplicationJsonAdapterFactory : JsonAdapter.Factory by KotshiApplicationJsonAdapterFactory