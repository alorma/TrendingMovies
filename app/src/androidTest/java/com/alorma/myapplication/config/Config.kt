package com.alorma.myapplication.config

import androidx.test.platform.app.InstrumentationRegistry
import com.alorma.myapplication.TrendingMoviesApp

val app: TrendingMoviesApp get() = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as TrendingMoviesApp