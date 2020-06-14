package com.rizafu.moviedb

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.rizafu.moviedb.data.ApiService
import com.rizafu.moviedb.data.Repository
import com.rizafu.moviedb.data.RepositoryImpl
import com.rizafu.moviedb.data.dao.MovieFavoriteDao
import com.rizafu.moviedb.data.entity.MovieFavoriteEntity
import com.rizafu.moviedb.data.model.MovieModel
import com.rizafu.moviedb.data.model.MovieOverviewModel
import com.rizafu.moviedb.data.model.MovieReviewModel
import com.rizafu.moviedb.data.model.PageResultModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
object RepoTest : Spek({
    val moviesOverview: PageResultModel<MovieOverviewModel> = mock {
        whenever(mock.results).thenReturn(listOf(MovieOverviewModel()))
    }
    val movieFavorite: MovieFavoriteEntity by memoized { MovieFavoriteEntity() }
    val moviesFavorite: List<MovieFavoriteEntity> by memoized { listOf(movieFavorite) }
    val movieDetail: MovieModel by memoized { MovieModel() }
    val movieReview: PageResultModel<MovieReviewModel> = mock {
        whenever(mock.results).thenReturn(listOf(MovieReviewModel()))
    }

    val apiService: ApiService = mock()

    val dao: MovieFavoriteDao = mock()

    val repo: Repository by memoized { RepositoryImpl(apiService, dao) }

    val movieId = 1

    beforeEachTest {
        runBlocking {
            whenever(apiService.getPopular()).thenReturn(moviesOverview)
            whenever(apiService.getNowPlaying()).thenReturn(moviesOverview)
            whenever(apiService.getTopRated()).thenReturn(moviesOverview)
            whenever(apiService.getUpcoming()).thenReturn(moviesOverview)
            whenever(apiService.getMovieDetail(movieId)).thenReturn(movieDetail)
            whenever(apiService.getMovieReviews(movieId)).thenReturn(movieReview)
            whenever(dao.getAll()).thenReturn(flow { emit(moviesFavorite) })
            whenever(dao.findById(movieId)).thenReturn(flow { emit(movieFavorite) })
        }
    }

    describe("repo success test") {

        it("popular") {
            runBlocking {
                repo.getPopular().collect {
                    assertEquals(it, moviesOverview)
                }
            }
        }

        it("now playing") {
            runBlocking {
                repo.getNowPlaying().collect {
                    assertEquals(it, moviesOverview)
                }
            }
        }

        it("top rated") {
            runBlocking {
                repo.getTopRated().collect {
                    assertEquals(it, moviesOverview)
                }
            }
        }

        it("upcoming") {
            runBlocking {
                repo.getUpcoming().collect {
                    assertEquals(it, moviesOverview)
                }
            }
        }

        it("favorite") {
            runBlocking {
                repo.getMoviesFavorite().collect {
                    assertEquals(it, moviesFavorite)
                }
            }
        }

        it("detail") {
            runBlocking {
                repo.getMovieDetail(movieId).collect {
                    assertEquals(it, movieDetail)
                }
            }
        }

        it("review") {
            runBlocking {
                repo.getMovieReviews(movieId).collect {
                    assertEquals(it, movieReview)
                }
            }
        }
    }
})