package com.rizafu.moviedb.ui.favorite

import androidx.lifecycle.viewModelScope
import com.rizafu.moviedb.data.EmptyType
import com.rizafu.moviedb.data.Repository
import com.rizafu.moviedb.data.Resource
import com.rizafu.moviedb.data.entity.MovieFavoriteEntity
import com.rizafu.moviedb.ui.base.BaseViewModel
import com.rizafu.moviedb.ui.viewholders.EmptyItemModel
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
class FavoriteViewModel @Inject constructor(private val repository: Repository) : BaseViewModel() {

    fun fetch() {
        job?.cancel()

        showLoading()

        job = viewModelScope.launch {
            repository.getMoviesFavorite()
                .map {
                    movieOverviewMapToItem(it)
                }.catch {
                    showError(it)
                }.collect {
                    items.postValue(Resource.success(it))
                }
        }
    }

    private fun movieOverviewMapToItem(moviesEntity: List<MovieFavoriteEntity>): List<ItemModel> {
        return if (moviesEntity.isEmpty()) {
            listOf(EmptyItemModel(EmptyType.NO_FAVORITE))
        } else {
            moviesEntity.map {
                MovieOverviewItemModel(
                    id = it.id,
                    title = it.title,
                    subTitle = it.releaseDate.formattedDate,
                    description = it.overview,
                    imageUrl = it.posterPath ?: it.backdropPath ?: ""
                )
            }
        }
    }
}