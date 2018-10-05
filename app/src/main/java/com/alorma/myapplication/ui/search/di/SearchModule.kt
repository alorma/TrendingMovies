package com.alorma.myapplication.ui.search.di

import androidx.lifecycle.ViewModelProviders
import androidx.fragment.app.FragmentActivity
import com.alorma.myapplication.ui.search.SearchNavigator
import com.alorma.myapplication.ui.search.SearchViewModel
import dagger.Module
import dagger.Provides

@Module
class SearchModule(val activity: androidx.fragment.app.FragmentActivity) {

    @Provides
    fun provideSearchNavigator(): SearchNavigator = SearchNavigator(activity)

    @Provides
    fun providesSearchViewModel(factory: SearchViewModelFactory): SearchViewModel =
            ViewModelProviders.of(activity, factory).get(SearchViewModel::class.java)
}