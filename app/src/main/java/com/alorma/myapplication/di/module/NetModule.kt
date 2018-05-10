package com.alorma.myapplication.di.module

import com.alorma.myapplication.data.net.ShowsApi
import com.alorma.myapplication.data.net.TvShowDto
import dagger.Module
import dagger.Provides
import io.reactivex.Single
import javax.inject.Singleton

@Module
class NetModule {

    @Provides
    @Singleton
    fun providesApi(): ShowsApi = object: ShowsApi {
        override fun listAll(): Single<List<TvShowDto>> = Single.never()
        override fun listPage(page: Int): Single<List<TvShowDto>> = Single.never()
    }
}