package com.alorma.myapplication.ui.detail.di

import com.alorma.myapplication.ui.detail.MovieDetailActivity
import dagger.Subcomponent

@Subcomponent(modules = [DetailModule::class])
interface DetailComponent {
    infix fun inject(detailActivity: MovieDetailActivity)
}