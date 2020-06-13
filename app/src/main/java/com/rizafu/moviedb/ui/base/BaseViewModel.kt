package com.rizafu.moviedb.ui.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rizafu.moviedb.data.Resource
import com.rizafu.moviedb.ui.viewholders.EmptyItemModel
import com.rizafu.moviedb.ui.viewholders.ErrorItemModel
import com.rizafu.moviedb.ui.viewholders.LoadingItemModel
import com.rizafu.moviedb.utils.ItemModel
import com.rizafu.moviedb.utils.errorMapper
import kotlinx.coroutines.Job

open class BaseViewModel : ViewModel() {
    protected val items = MutableLiveData<Resource<List<ItemModel>>>()
    protected var job: Job? = null

    protected fun showError(throwable: Throwable) {
        throwable.printStackTrace()
        items.postValue(
            Resource.error(
                throwable.message,
                listOf(ErrorItemModel(throwable.errorMapper()))
            )
        )
    }

    protected fun showLoading(isLoadMore: Boolean = false) {
        val loadingItems: List<ItemModel> = if (isLoadMore) {
            val currentItems = items.value?.data?.toMutableList() ?: mutableListOf()
            currentItems.removeAll { it is LoadingItemModel || it is EmptyItemModel || it is ErrorItemModel }
            currentItems.add(LoadingItemModel())
            currentItems
        } else {
            listOf(LoadingItemModel())
        }
        items.postValue(Resource.loading(loadingItems))
    }
}