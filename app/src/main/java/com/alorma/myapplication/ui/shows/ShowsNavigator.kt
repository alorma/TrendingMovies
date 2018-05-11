package com.alorma.myapplication.ui.shows

import android.app.Activity
import android.content.Intent
import com.alorma.myapplication.ui.common.Navigator
import com.alorma.myapplication.ui.detail.ShowDetailActivity

class ShowsNavigator(activity: Activity, private val routes: ShowsRoutes) :
        Navigator<ShowsRoutes.ShowsRoute>(activity) {

    override fun navigate(route: ShowsRoutes.ShowsRoute) {
        when (route) {
            is ShowsRoutes.ShowsRoute.DetailRoute -> openDetail(route.id)
        }
    }

    private fun openDetail(id: Int) {
        val intent = Intent(activity, ShowDetailActivity::class.java)
        start(intent)
    }
}