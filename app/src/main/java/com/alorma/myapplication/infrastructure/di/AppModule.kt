package com.alorma.myapplication.infrastructure.di

import com.bumptech.glide.Glide
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module

val appModule = module {
    factory { Glide.with(androidContext()) }
}