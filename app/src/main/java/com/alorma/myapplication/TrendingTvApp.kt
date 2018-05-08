package com.alorma.myapplication

import android.app.Application
import com.alorma.myapplication.di.ApplicationComponent
import com.alorma.myapplication.di.module.ApplicationModule

class TrendingTvApp  : Application() {

    companion object {
        lateinit var component: ApplicationComponent
    }

    override fun onCreate() {
        super.onCreate()

        component = DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .build()
    }
}
