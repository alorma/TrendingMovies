package com.alorma.myapplication.config

import android.support.test.InstrumentationRegistry
import com.alorma.myapplication.TrendingTvApp
import com.alorma.myapplication.di.ApplicationComponent
import com.alorma.myapplication.di.module.ApplicationModule
import it.cosenonjaviste.daggermock.DaggerMock

fun getDaggerRule() = DaggerMock.rule<ApplicationComponent>(ApplicationModule(app)) {
    set { component ->
        app.updateComponent(component)
    }
}

val app: TrendingTvApp get() = InstrumentationRegistry.getTargetContext().applicationContext as TrendingTvApp