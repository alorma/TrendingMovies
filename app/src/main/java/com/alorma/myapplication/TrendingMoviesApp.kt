package com.alorma.myapplication

import android.app.Application
import com.alorma.myapplication.infrastructure.di.*
import org.koin.android.ext.android.startKoin

class TrendingMoviesApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin(this, listOf(
                appModule,
                coreModule,
                domainModule,
                localDataModule,
                networkModule
        ))
    }
}
