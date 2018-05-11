package com.alorma.myapplication.ui.splash.di

import com.alorma.myapplication.ui.splash.SplashActivity
import dagger.Subcomponent

@Subcomponent(modules = [SplashModule::class])
interface SplashComponent {
    infix fun inject(splashActivity: SplashActivity)
}