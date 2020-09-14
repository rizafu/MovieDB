package com.rizafu.moviedb.ui.main

import androidx.lifecycle.viewModelScope
import com.rizafu.moviedb.data.EmptyType
import com.rizafu.moviedb.data.MoviesOverviewType
import com.rizafu.moviedb.data.Repository
import com.rizafu.moviedb.data.Resource
import com.rizafu.moviedb.data.model.MovieOverviewModel
import com.rizafu.moviedb.data.model.PageResultModel
import com.rizafu.moviedb.ui.base.BaseViewModel
import com.rizafu.moviedb.ui.viewholders.EmptyItemModel
import com.rizafu.moviedb.ui.viewholders.ErrorItemModel
import com.rizafu.moviedb.ui.viewholders.LoadingItemModel
import com.rizafu.moviedb.ui.viewholders.MovieOverviewItemModel
import com.rizafu.moviedb.utils.ItemModel
import com.rizafu.moviedb.utils.formattedDate
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
class MainViewModel @Inject constructor(private val repository: Repository) : BaseViewModel() {
    private var page: Int = 1

    fun fetch(movieOverviewType: MoviesOverviewType, isLoadMore: Boolean = false) {
        if (isLoadMore) page++ else page = 1

        job?.cancel()

        showLoading(isLoadMore)

        job = viewModelScope.launch {
            when (movieOverviewType) {
                MoviesOverviewType.POPULAR -> repository.getPopular(page)
                MoviesOverviewType.TOP_RATED -> repository.getTopRated(page)
                MoviesOverviewType.NOW_PLAYING -> repository.getNowPlaying(page)
                MoviesOverviewType.UPCOMING -> repository.getUpcoming(page)
            }.map {
                movieOverviewMapToItem(it)
            }.catch {
                showError(it)
            }.collect {
                items.postValue(Resource.success(it))
            }
        }
    }

    private fun movieOverviewMapToItem(
        moviesResult: PageResultModel<MovieOverviewModel>
    ): List<ItemModel> {
        val newItems = moviesResult.results.map {
            MovieOverviewItemModel(
                id = it.id,
                title = it.title,
                subTitle = it.releaseDate.formattedDate,
                description = it.overview,
                imageUrl = it.posterPath ?: it.backdropPath ?: ""
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

    fun isNoMoreDataToLoad(items: List<ItemModel>): Boolean =
        items.contains(EmptyItemModel(EmptyType.NO_MORE))
}