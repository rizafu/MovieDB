package com.rizafu.moviedb.di.builder

import com.rizafu.moviedb.ui.detail.DetailActivity
import com.rizafu.moviedb.ui.favorite.FavoriteActivity
import com.rizafu.moviedb.ui.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@Module
abstract class ActivityBuilder {

    @ExperimentalCoroutinesApi
    @ContributesAndroidInjector
    abstract fun bindMainActivity(): MainActivity

    @ExperimentalCoroutinesApi
    @ContributesAndroidInjector
    abstract fun bindDetailActivity(): DetailActivity

    @ExperimentalCoroutinesApi
    @ContributesAndroidInjector
    abstract fun bindFavoriteActivity(): FavoriteActivity
}