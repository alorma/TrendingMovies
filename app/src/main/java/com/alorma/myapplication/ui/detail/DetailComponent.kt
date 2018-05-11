package com.alorma.myapplication.ui.detail

import com.alorma.myapplication.ui.detail.di.DetailModule
import dagger.Subcomponent

@Subcomponent(modules = [DetailModule::class])
interface DetailComponent {
    infix fun inject(detailActivity: ShowDetailActivity)
}