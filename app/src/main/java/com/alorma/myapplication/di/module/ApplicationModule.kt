package com.alorma.myapplication.di.module

import android.arch.paging.PagedList
import android.content.Context
import com.alorma.myapplication.domain.model.Movie
import com.alorma.myapplication.domain.repository.ConfigurationRepository
import com.alorma.myapplication.domain.repository.MoviesRepository
import com.alorma.myapplication.domain.repository.PageListMovieBoundaryCallback
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import com.alorma.myapplication.data.cache.ConfigDataSource as ConfigCache
import com.alorma.myapplication.data.cache.MoviesDataSource as Cache
import com.alorma.myapplication.data.net.config.ConfigDataSource as ConfigNetwork
import com.alorma.myapplication.data.net.MoviesDataSource as Network

@Module
class ApplicationModule(private val context: Context) {

    @Provides
    @Singleton
    fun providesContext(): Context = context

    @Provides
    @Singleton
    fun getGlide(): RequestManager = Glide.with(context)

    @Provides
    fun providesPageCallback(network: Network, cache: Cache): PagedList.BoundaryCallback<Movie> =
            PageListMovieBoundaryCallback(network, cache)

    @Provides
    fun getMoviesRepository(cache: Cache, callback: PagedList.BoundaryCallback<Movie>): MoviesRepository {
        return MoviesRepository(cache, callback)
    }

    @Provides
    fun getConfigurationRepository(network: ConfigNetwork, cache: ConfigCache): ConfigurationRepository =
            ConfigurationRepository(network, cache)
}