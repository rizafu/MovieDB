package com.rizafu.moviedb.ui.base

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.rizafu.moviedb.ui.viewholders.*
import com.rizafu.moviedb.utils.RecyclerViewListAdapter
import dagger.android.AndroidInjection
import javax.inject.Inject

open class BaseActivity : AppCompatActivity() {

    protected val adapter: RecyclerViewListAdapter = RecyclerViewListAdapter()
        .registerViewHolderFactory(EmptyViewHolderFactory())
        .registerViewHolderFactory(ErrorViewHolderFactory())
        .registerViewHolderFactory(LoadingViewHolderFactory())
        .registerViewHolderFactory(MovieDetailViewHolderFactory())
        .registerViewHolderFactory(MovieOverviewViewHolderFactory())
        .registerViewHolderFactory(MovieReviewViewHolderFactory())

    @Inject
    protected lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}