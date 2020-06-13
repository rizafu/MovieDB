package com.rizafu.moviedb.ui.favorite

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.rizafu.moviedb.R
import com.rizafu.moviedb.databinding.BaseRecyclerViewBinding
import com.rizafu.moviedb.ui.base.BaseActivity
import com.rizafu.moviedb.ui.detail.DetailActivity
import com.rizafu.moviedb.ui.viewholders.ErrorItemModel
import com.rizafu.moviedb.ui.viewholders.MovieOverviewItemModel
import com.rizafu.moviedb.utils.ItemModel
import com.rizafu.moviedb.utils.setupLinearVertical
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
class FavoriteActivity : BaseActivity() {
    private val binding: BaseRecyclerViewBinding by lazy {
        BaseRecyclerViewBinding.inflate(
            layoutInflater
        )
    }
    private lateinit var favoriteViewModel: FavoriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupUI()
        setupViewModel()
        setupObservable()
    }

    private fun setupViewModel() {
        favoriteViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(FavoriteViewModel::class.java)
        favoriteViewModel.fetch()
    }

    private fun setupUI() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.favorite)

        binding.recyclerView.setupLinearVertical(
            this,
            adapter,
            onItemClick = { _: View, itemModel: ItemModel ->
                when (itemModel) {
                    is MovieOverviewItemModel -> {
                        DetailActivity.startActivity(this, itemModel.id)
                    }
                    is ErrorItemModel -> {
                        favoriteViewModel.fetch()
                    }
                }
            })
    }

    private fun setupObservable() {
        favoriteViewModel.getItems().observe(this, Observer {
            adapter.submitList(it.data)
        })
    }

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, FavoriteActivity::class.java))
        }
    }
}