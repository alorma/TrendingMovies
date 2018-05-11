package com.alorma.myapplication.ui.shows

import android.app.Activity
import android.widget.Toast
import com.alorma.myapplication.ui.common.Navigator

class ShowsNavigator(activity: Activity) : Navigator<ShowsRoutes.ShowsRoute>(activity) {
    override fun navigate(route: ShowsRoutes.ShowsRoute) {
        when (route) {
            is ShowsRoutes.ShowsRoute.DetailRoute -> openDetail(route.id)
        }
    }

    private fun openDetail(id: Int) {
        Toast.makeText(activity, "Open detail: $id", Toast.LENGTH_SHORT).show()
    }
}