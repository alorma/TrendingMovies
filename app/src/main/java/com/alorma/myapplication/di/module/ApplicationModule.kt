package com.alorma.myapplication.di.module

import android.content.Context
import com.alorma.myapplication.domain.repository.ConfigurationRepository
import com.alorma.myapplication.domain.repository.ShowsRepository
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import com.alorma.myapplication.data.cache.ShowsDataSource as Cache
import com.alorma.myapplication.data.net.ConfigDataSource as ConfigNetwork
import com.alorma.myapplication.data.net.ShowsDataSource as Network

@Module
class ApplicationModule(private val context: Context) {

    @Provides
    @Singleton
    fun providesContext(): Context = context

    @Provides
    @Singleton
    fun getGlide(): RequestManager = Glide.with(context)

    @Provides
    fun getShowsRepository(network: Network, cache: Cache): ShowsRepository =
            ShowsRepository(network, cache)

    @Provides
    fun getConfigurationRepository(network: ConfigNetwork): ConfigurationRepository =
            ConfigurationRepository(network)
}