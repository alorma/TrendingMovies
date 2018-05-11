package com.alorma.myapplication.ui.splash

import android.app.Activity
import android.content.Intent
import com.alorma.myapplication.ui.common.Navigator
import com.alorma.myapplication.ui.shows.ShowsActivity

class SplashNavigator(activity: Activity) : Navigator<SplashRoutes.SplashRoute>(activity) {

    override fun navigate(route: SplashRoutes.SplashRoute) {
        when (route) {
            is SplashRoutes.SplashRoute.Main -> openMain()
        }
    }

    private fun openMain() = start(Intent(activity, ShowsActivity::class.java))
}