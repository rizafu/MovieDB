package com.rizafu.moviedb.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rizafu.moviedb.data.EmptyType
import com.rizafu.moviedb.data.Repository
import com.rizafu.moviedb.data.Resource
import com.rizafu.moviedb.data.entity.MovieFavoriteEntity
import com.rizafu.moviedb.data.model.MovieModel
import com.rizafu.moviedb.data.model.MovieReviewModel
import com.rizafu.moviedb.data.model.PageResultModel
import com.rizafu.moviedb.ui.base.BaseViewModel
import com.rizafu.moviedb.ui.viewholders.*
import com.rizafu.moviedb.utils.ItemModel
import com.rizafu.moviedb.utils.formattedDate
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@FlowPreview
class DetailViewModel @Inject constructor(private val repository: Repository) : BaseViewModel() {
    private lateinit var movieModel: MovieModel
    private val movieTitle = MutableLiveData<String>()
    private var page: Int = 1

    fun fetch(movieId: Int, isLoadMore: Boolean = false) {
        if (isLoadMore) page++ else page = 1

        job?.cancel()

        showLoading(isLoadMore)

        job = viewModelScope.launch {
            if (isLoadMore) {
                fetchReviews(movieId)
            } else {
                fetchDetailAndReview(movieId)
            }.catch {
                showError(it)
            }.collect {
                items.postValue(Resource.success(it))
            }
        }
    }

    private suspend fun fetchReviews(movieId: Int): Flow<List<ItemModel>> {
        return repository.getMovieReviews(movieId, page).map {
            movieReviewsMapToItem(it)
        }
    }

    private suspend fun fetchDetailAndReview(movieId: Int): Flow<List<ItemModel>> {
        return repository.getMovieDetail(movieId)
            .flatMapConcat { movie ->
                repository.isMovieFavorite(movie.id).map { it to movie }
            }.map {
                val (isFavorite, movie) = it
                movieModel = movie
                movieTitle.postValue(movie.title)
                movieDetailMapToItem(isFavorite, movie)
            }.flatMapConcat { movieDetailItem ->
                val movieDetail: MutableList<ItemModel> = movieDetailItem.toMutableList()
                fetchReviews(movieId).map {
                    movieDetail.addAll(it)
                    return@map movieDetail
                }
            }
    }

    private fun movieReviewsMapToItem(moviesResult: PageResultModel<MovieReviewModel>): List<ItemModel> {
        val newItems = moviesResult.results.map {
            MovieReviewItemModel(
                id = it.id,
                title = it.author,
                description = it.content
            )
        }

        val oldItems = items.value?.data?.toMutableList() ?: mutableListOf()
        oldItems.removeAll { it is LoadingItemModel || it is EmptyItemModel || it is ErrorItemModel }

        return mutableListOf<ItemModel>().apply {
            addAll(oldItems)
            addAll(newItems)
            if (moviesResult.page >= moviesResult.totalPages) {
                add(EmptyItemModel(EmptyType.NO_MORE))
            }
        }
    }

    private fun movieDetailMapToItem(isFavorite: Boolean, moviesResult: MovieModel) =
        listOf<ItemModel>(
            MovieDetailItemModel(
                id = moviesResult.id,
                title = moviesResult.title,
                description = moviesResult.overview ?: "",
                imageUrl = moviesResult.posterPath ?: "",
                imageBackdropUrl = moviesResult.backdropPath ?: "",
                isFavorite = isFavorite,
                subTitle = moviesResult.releaseDate.formattedDate
            )
        )

    fun isNoMoreDataToLoad(items: List<ItemModel>): Boolean =
        items.contains(EmptyItemModel(EmptyType.NO_MORE))

    fun getMovieTitle(): LiveData<String> = movieTitle

    fun saveOrRemoveToFavorite(isFavorite: Boolean) {
        job?.cancel()
        job = viewModelScope.launch {
            repository.saveOrDeleteMovieFavorite(
                isFavorite, MovieFavoriteEntity(
                    id = movieModel.id,
                    title = movieModel.title,
                    adult = movieModel.adult,
                    backdropPath = movieModel.backdropPath,
                    originalLanguage = movieModel.originalLanguage,
                    originalTitle = movieModel.originalTitle,
                    overview = movieModel.overview ?: "",
                    popularity = movieModel.popularity,
                    posterPath = movieModel.posterPath,
                    releaseDate = movieModel.releaseDate,
                    video = movieModel.video,
                    voteAverage = movieModel.voteAverage,
                    voteCount = movieModel.voteCount
                )
            )
        }
    }
}