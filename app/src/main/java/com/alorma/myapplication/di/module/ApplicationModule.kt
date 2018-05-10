package com.alorma.myapplication.di.module

import android.content.Context
import com.alorma.myapplication.data.net.ShowsDataSource
import com.alorma.myapplication.domain.repository.ShowsRepository
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(private val context: Context) {

    @Provides
    @Singleton
    fun providesContext(): Context = context

    @Provides
    @Singleton
    fun getGlide(): RequestManager = Glide.with(context)

    @Provides
    fun getShowsRepository(dataSource: ShowsDataSource): ShowsRepository = ShowsRepository(dataSource)
}