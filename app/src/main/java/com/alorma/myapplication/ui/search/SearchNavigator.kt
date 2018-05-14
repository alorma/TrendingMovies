package com.alorma.myapplication.ui.search

import android.app.Activity
import com.alorma.myapplication.ui.common.Navigator
import com.alorma.myapplication.ui.detail.MovieDetailActivity

class SearchNavigator(activity: Activity) : Navigator<SearchRoutes.SearchRoute>(activity) {
    override fun navigate(route: SearchRoutes.SearchRoute) {
        when (route) {
            is SearchRoutes.SearchRoute.OpenDetail -> openDetail(route)
        }
    }

    private fun openDetail(route: SearchRoutes.SearchRoute.OpenDetail) =
            start(MovieDetailActivity.launch(activity, route.id, route.title))
}