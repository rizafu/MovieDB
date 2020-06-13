package com.rizafu.moviedb.di.module

import androidx.lifecycle.ViewModelProvider
import com.rizafu.moviedb.ui.ViewModelFactory
import dagger.Binds
import dagger.Module

@Module
interface ViewModelFactoryModule {

    @Binds
    fun bindsViewModelFactory(providerFactory: ViewModelFactory): ViewModelProvider.Factory
}