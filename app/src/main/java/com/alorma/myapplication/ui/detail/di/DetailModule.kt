package com.alorma.myapplication.ui.detail.di

import androidx.lifecycle.ViewModelProviders
import androidx.fragment.app.FragmentActivity
import com.alorma.myapplication.ui.detail.DetailNavigator
import com.alorma.myapplication.ui.detail.MovieDetailViewModel
import dagger.Module
import dagger.Provides

@Module
class DetailModule(private val activity: androidx.fragment.app.FragmentActivity) {

    @Provides
    fun provideNavigator(): DetailNavigator = DetailNavigator(activity)

    @Provides
    fun providesViewModel(factory: DetailViewModelFactory): MovieDetailViewModel =
            ViewModelProviders.of(activity, factory).get(MovieDetailViewModel::class.java)
}