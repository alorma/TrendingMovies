package com.alorma.myapplication.di.module

import android.content.Context
import dagger.Module
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(private val context: Context) {
    @Provides
    @Singleton
    fun getGlide(): RequestManager = Glide.with(context)

}