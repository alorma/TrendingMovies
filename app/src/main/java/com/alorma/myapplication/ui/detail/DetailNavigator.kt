package com.alorma.myapplication.ui.detail

import android.app.Activity
import com.alorma.myapplication.ui.common.Navigator

class DetailNavigator(activity: Activity) :
        Navigator<DetailRoutes.DetailRoute>(activity) {

    override fun navigate(route: DetailRoutes.DetailRoute) {
        when(route) {
            DetailRoutes.DetailRoute.Back -> activity.finish()
        }
    }
}