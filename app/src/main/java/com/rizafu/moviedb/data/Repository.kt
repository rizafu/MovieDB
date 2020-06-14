package com.rizafu.moviedb.data

import com.rizafu.moviedb.data.dao.MovieFavoriteDao
import com.rizafu.moviedb.data.entity.MovieFavoriteEntity
import com.rizafu.moviedb.data.model.MovieModel
import com.rizafu.moviedb.data.model.MovieOverviewModel
import com.rizafu.moviedb.data.model.MovieReviewModel
import com.rizafu.moviedb.data.model.PageResultModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface Repository {

    suspend fun getPopular(
        page: Int = 1,
        language: String = "en-US"
    ): Flow<PageResultModel<MovieOverviewModel>>

    suspend fun getTopRated(
        page: Int = 1,
        language: String = "en-US"
    ): Flow<PageResultModel<MovieOverviewModel>>

    suspend fun getNowPlaying(
        page: Int = 1,
        language: String = "en-US"
    ): Flow<PageResultModel<MovieOverviewModel>>

    suspend fun getUpcoming(
        page: Int = 1,
        language: String = "en-US"
    ): Flow<PageResultModel<MovieOverviewModel>>

    suspend fun getMovieDetail(
        movieId: Int,
        language: String = "en-US"
    ): Flow<MovieModel>

    suspend fun getMovieReviews(
        movieId: Int,
        page: Int = 1,
        language: String = "en-US"
    ): Flow<PageResultModel<MovieReviewModel>>

    suspend fun getMoviesFavorite(): Flow<List<MovieFavoriteEntity>>

    suspend fun saveOrDeleteMovieFavorite(
        isFavorite: Boolean,
        movieFavoriteEntity: MovieFavoriteEntity
    )

    suspend fun isMovieFavorite(movieId: Int): Flow<Boolean>
}

@ExperimentalCoroutinesApi
class RepositoryImpl @Inject constructor(
    private var apiService: ApiService,
    private var movieFavoriteDao: MovieFavoriteDao
) : Repository {

    override suspend fun getPopular(
        page: Int,
        language: String
    ): Flow<PageResultModel<MovieOverviewModel>> = flow {
        emit(apiService.getPopular(page, language))
    }.flowOn(Dispatchers.IO)

    override suspend fun getTopRated(
        page: Int,
        language: String
    ): Flow<PageResultModel<MovieOverviewModel>> = flow {
        emit(apiService.getTopRated(page, language))
    }.flowOn(Dispatchers.IO)

    override suspend fun getNowPlaying(
        page: Int,
        language: String
    ): Flow<PageResultModel<MovieOverviewModel>> = flow {
        emit(apiService.getNowPlaying(page, language))
    }.flowOn(Dispatchers.IO)

    override suspend fun getUpcoming(
        page: Int,
        language: String
    ): Flow<PageResultModel<MovieOverviewModel>> = flow {
        emit(apiService.getUpcoming(page, language))
    }.flowOn(Dispatchers.IO)

    override suspend fun getMovieDetail(movieId: Int, language: String): Flow<MovieModel> =
        flow {
            emit(apiService.getMovieDetail(movieId, language))
        }.flowOn(Dispatchers.IO)

    override suspend fun getMovieReviews(
        movieId: Int,
        page: Int,
        language: String
    ): Flow<PageResultModel<MovieReviewModel>> = flow {
        emit(apiService.getMovieReviews(movieId, page, language))
    }.flowOn(Dispatchers.IO)

    override suspend fun getMoviesFavorite(): Flow<List<MovieFavoriteEntity>> {
        return movieFavoriteDao.getAll()
    }

    override suspend fun saveOrDeleteMovieFavorite(
        isFavorite: Boolean,
        movieFavoriteEntity: MovieFavoriteEntity
    ) {
        withContext(Dispatchers.IO) {
            if (isFavorite) {
                movieFavoriteDao.insert(movieFavoriteEntity)
            } else {
                movieFavoriteDao.deleteById(movieId = movieFavoriteEntity.id)
            }
        }
    }

    override suspend fun isMovieFavorite(movieId: Int): Flow<Boolean> = flow {
        movieFavoriteDao.findById(movieId = movieId)
            .catch {
                emit(false)
            }.collect {
                emit(it != null)
            }
    }.flowOn(Dispatchers.IO)

}