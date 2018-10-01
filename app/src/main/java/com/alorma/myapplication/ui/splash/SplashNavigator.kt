package com.alorma.myapplication.ui.splash

import android.app.Activity
import android.content.Intent
import com.alorma.myapplication.ui.common.Navigator
import com.alorma.myapplication.ui.movies.MoviesActivity

class SplashNavigator(activity: Activity) : Navigator<SplashRoutes.SplashRoute>(activity) {

    override fun navigate(route: SplashRoutes.SplashRoute) {
        when (route) {
            SplashRoutes.SplashRoute.Main -> openMain()
            SplashRoutes.SplashRoute.Error -> onError()
        }
    }

    private fun openMain() = start(Intent(activity, MoviesActivity::class.java)).also {
        activity.finish()
    }

    private fun onError() = start(Intent(activity, MoviesActivity::class.java)).also {
        activity.finish()
    }
}