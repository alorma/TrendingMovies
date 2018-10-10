package com.alorma.myapplication.infrastructure.di

import com.alorma.data.net.DateParser
import com.alorma.domain.common.ResourceProvider
import com.alorma.myapplication.ui.common.ResourcesProviderImpl
import com.alorma.presentation.common.DateFormatter
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module

val coreModule = module {
    factory<ResourceProvider> { ResourcesProviderImpl(androidContext()) }
    factory { DateParser() }
    factory { DateFormatter() }
}