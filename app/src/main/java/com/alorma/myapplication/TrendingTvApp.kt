package com.alorma.myapplication

import android.app.Application
import com.alorma.myapplication.di.ApplicationComponent
import com.alorma.myapplication.di.DaggerApplicationComponent
import com.alorma.myapplication.di.module.ApplicationModule
import com.alorma.myapplication.di.module.NetModule

class TrendingTvApp : Application() {

    companion object {
        lateinit var component: ApplicationComponent
    }

    override fun onCreate() {
        super.onCreate()

        component = DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .netModule(NetModule(this))
                .build()
    }

    fun updateComponent(updateComponent: ApplicationComponent) {
        component = updateComponent
    }
}
