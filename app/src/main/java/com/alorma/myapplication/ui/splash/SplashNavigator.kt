package com.alorma.myapplication.ui.splash

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import com.alorma.myapplication.R
import com.alorma.myapplication.ui.common.Navigator
import com.alorma.myapplication.ui.shows.ShowsActivity

class SplashNavigator(activity: Activity) : Navigator<SplashRoutes.SplashRoute>(activity) {

    override fun navigate(route: SplashRoutes.SplashRoute) {
        when (route) {
            SplashRoutes.SplashRoute.Main -> openMain()
            SplashRoutes.SplashRoute.Error -> onError()
        }
    }

    private fun openMain() = start(Intent(activity, ShowsActivity::class.java)).also {
        activity.finish()
    }

    private fun onError() {
        Toast.makeText(activity, R.string.generic_error, Toast.LENGTH_SHORT).show()
        activity.finish()
    }
}