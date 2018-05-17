package com.alorma.myapplication.ui.detail.di

import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.FragmentActivity
import com.alorma.myapplication.ui.detail.DetailNavigator
import com.alorma.myapplication.ui.detail.MovieDetailViewModel
import dagger.Module
import dagger.Provides

@Module
class DetailModule(private val activity: FragmentActivity) {

    @Provides
    fun provideNavigator(): DetailNavigator = DetailNavigator(activity)

    @Provides
    fun providesViewModel(factory: DetailViewModelFactory): MovieDetailViewModel =
            ViewModelProviders.of(activity, factory).get(MovieDetailViewModel::class.java)
}