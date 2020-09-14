package com.rizafu.moviedb.ui.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.rizafu.moviedb.R
import com.rizafu.moviedb.data.MoviesOverviewType
import com.rizafu.moviedb.data.Status
import com.rizafu.moviedb.databinding.ActivityMainBinding
import com.rizafu.moviedb.databinding.DialogCategoryBinding
import com.rizafu.moviedb.ui.base.BaseActivity
import com.rizafu.moviedb.ui.detail.DetailActivity
import com.rizafu.moviedb.ui.favorite.FavoriteActivity
import com.rizafu.moviedb.ui.viewholders.ErrorItemModel
import com.rizafu.moviedb.ui.viewholders.MovieOverviewItemModel
import com.rizafu.moviedb.utils.ItemModel
import com.rizafu.moviedb.utils.setupLinearVertical
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@ExperimentalCoroutinesApi
@FlowPreview
class MainActivity : BaseActivity() {
    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var mainViewModel: MainViewModel
    private var moviesOverviewType: MoviesOverviewType = MoviesOverviewType.POPULAR

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupUI()
        setupViewModel()
        setupObservable()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_mode -> {
                val mode =
                    if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                        AppCompatDelegate.MODE_NIGHT_NO
                    } else {
                        AppCompatDelegate.MODE_NIGHT_YES
                    }
                AppCompatDelegate.setDefaultNightMode(mode)
                true
            }
            R.id.menu_favorite -> {
                FavoriteActivity.startActivity(this)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun newFetch() {
        mainViewModel.fetch(moviesOverviewType)
        adapter.isStopLoadMore(false)
    }

    private fun setupViewModel() {
        mainViewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
        newFetch()
    }

    private fun setupUI() {
        binding.recyclerView.setupLinearVertical(
            adapter,
            onItemClick = { _: View, itemModel: ItemModel ->
                when (itemModel) {
                    is MovieOverviewItemModel -> {
                        DetailActivity.startActivity(this, itemModel.id)
                    }
                    is ErrorItemModel -> {
                        newFetch()
                    }
                }
            })

        adapter.setLoadMore(binding.recyclerView) {
            mainViewModel.fetch(moviesOverviewType, isLoadMore = true)
        }

        binding.buttonCategory.setOnClickListener {
            BottomSheetDialog(this).apply {
                val dialogCategoryBinding = DialogCategoryBinding.inflate(layoutInflater)
                setContentView(dialogCategoryBinding.root)

                dialogCategoryBinding.radioGroup.check(
                    when (moviesOverviewType) {
                        MoviesOverviewType.POPULAR -> R.id.radio_popular
                        MoviesOverviewType.TOP_RATED -> R.id.radio_top_rated
                        MoviesOverviewType.NOW_PLAYING -> R.id.radio_now_playing
                        MoviesOverviewType.UPCOMING -> R.id.radio_upcoming
                    }
                )

                dialogCategoryBinding.radioGroup.setOnCheckedChangeListener { _, id ->
                    moviesOverviewType = when (id) {
                        R.id.radio_popular -> MoviesOverviewType.POPULAR
                        R.id.radio_top_rated -> MoviesOverviewType.TOP_RATED
                        R.id.radio_now_playing -> MoviesOverviewType.NOW_PLAYING
                        R.id.radio_upcoming -> MoviesOverviewType.UPCOMING
                        else -> MoviesOverviewType.POPULAR
                    }
                    dismiss()
                    newFetch()
                }
            }.show()
        }
    }

    private fun setupObservable() {
        mainViewModel.getItems().observe(this, Observer {
            adapter.submitList(it.data)

            when (it.status) {
                Status.SUCCESS -> {
                    adapter.isLoading(false)
                    adapter.isStopLoadMore(mainViewModel.isNoMoreDataToLoad(it.data ?: listOf()))
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
}