package com.rizafu.moviedb.ui.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.rizafu.moviedb.data.Status
import com.rizafu.moviedb.databinding.BaseRecyclerViewBinding
import com.rizafu.moviedb.ui.base.BaseActivity
import com.rizafu.moviedb.ui.viewholders.ErrorItemModel
import com.rizafu.moviedb.ui.viewholders.MovieDetailItemModel
import com.rizafu.moviedb.utils.ItemModel
import com.rizafu.moviedb.utils.setupLinearVertical
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@ExperimentalCoroutinesApi
@FlowPreview
class DetailActivity : BaseActivity() {
    private val binding: BaseRecyclerViewBinding by lazy {
        BaseRecyclerViewBinding.inflate(
            layoutInflater
        )
    }
    private val movieId: Int by lazy { intent.extras?.getInt(MOVIE_ID, -1) ?: -1 }

    private lateinit var detailViewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupUI()
        setupViewModel()
        setupObservable()
    }

    private fun newFetch() {
        detailViewModel.fetch(movieId)
        adapter.isStopLoadMore(false)
    }

    private fun setupViewModel() {
        detailViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(DetailViewModel::class.java)
        newFetch()
    }

    private fun setupUI() {
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = ""
        }
        binding.recyclerView.setupLinearVertical(
            adapter,
            onItemClick = { _: View, itemModel: ItemModel ->
                when (itemModel) {
                    is MovieDetailItemModel -> {
                        detailViewModel.saveOrRemoveToFavorite(itemModel.isFavorite)
                    }
                    is ErrorItemModel -> {
                        newFetch()
                    }
                }
            })

        adapter.setLoadMore(binding.recyclerView) {
            detailViewModel.fetch(movieId, isLoadMore = true)
        }
    }

    private fun setupObservable() {
        detailViewModel.getMovieTitle().observe(this, Observer {
            supportActionBar?.title = it
        })

        detailViewModel.getItems().observe(this, Observer {
            adapter.submitList(it.data)

            when (it.status) {
                Status.SUCCESS -> {
                    adapter.isLoading(false)
                    adapter.isStopLoadMore(detailViewModel.isNoMoreDataToLoad(it.data ?: listOf()))
                }
                Status.ERROR -> {
                    adapter.isStopLoadMore(true)
                }
                Status.LOADING -> {
                    adapter.isLoading(true)
                }
            }
        })
    }

    companion object {
        private const val MOVIE_ID = "MOVIE_ID"
        fun startActivity(context: Context, movieId: Int) {
            context.startActivity(Intent(context, DetailActivity::class.java).also {
                it.putExtra(MOVIE_ID, movieId)
            })
        }
    }
}