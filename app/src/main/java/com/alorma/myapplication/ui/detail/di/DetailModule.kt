package com.alorma.myapplication.ui.detail.di

import android.app.Activity
import com.alorma.myapplication.ui.common.Navigator
import com.alorma.myapplication.ui.detail.DetailNavigator
import com.alorma.myapplication.ui.detail.DetailRoutes
import dagger.Module
import dagger.Provides

@Module
class DetailModule(private val activity: Activity) {

    @Provides
    fun provideNavigator(): DetailNavigator = DetailNavigator(activity)

}