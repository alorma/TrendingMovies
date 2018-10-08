package com.alorma.myapplication.infrastructure.di

import com.alorma.data.net.DateParser
import com.alorma.myapplication.ui.common.DateFormatter
import com.alorma.myapplication.ui.common.ResourcesProvider
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module

val coreModule = module {
    factory { ResourcesProvider(androidContext()) }
    factory { DateParser() }
    factory { DateFormatter() }
}