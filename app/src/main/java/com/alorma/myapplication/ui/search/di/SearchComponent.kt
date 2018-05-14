package com.alorma.myapplication.ui.search.di

import com.alorma.myapplication.ui.search.SearchActivity
import dagger.Subcomponent

@Subcomponent(modules = [SearchModule::class])
interface SearchComponent {
    infix fun inject(activity: SearchActivity)
}