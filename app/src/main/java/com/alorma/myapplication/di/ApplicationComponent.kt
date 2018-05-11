package com.alorma.myapplication.di

import com.alorma.myapplication.di.module.ApplicationModule
import com.alorma.myapplication.di.module.NetModule
import com.alorma.myapplication.ui.shows.ShowsActivity
import com.alorma.myapplication.ui.shows.di.ShowsComponent
import com.alorma.myapplication.ui.shows.di.ShowsModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, NetModule::class])
interface ApplicationComponent {
    infix fun add(module: ShowsModule): ShowsComponent
}