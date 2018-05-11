package com.alorma.myapplication.ui.shows

import android.app.Activity
import com.alorma.myapplication.ui.common.Navigator
import com.alorma.myapplication.ui.detail.ShowDetailActivity

class ShowsNavigator(activity: Activity) : Navigator<ShowsRoutes.ShowsRoute>(activity) {

    override fun navigate(route: ShowsRoutes.ShowsRoute) {
        when (route) {
            is ShowsRoutes.ShowsRoute.DetailRoute -> openDetail(route)
        }
    }

    private fun openDetail(route: ShowsRoutes.ShowsRoute.DetailRoute)
            = start(ShowDetailActivity.launch(activity, route.id, route.title))
}