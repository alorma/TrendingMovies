package com.alorma.myapplication.ui.shows.di

import com.alorma.myapplication.ui.shows.ShowsActivity
import dagger.Subcomponent

@Subcomponent(modules = [ShowsModule::class])
interface ShowsComponent {
    infix fun inject(activity: ShowsActivity)
}