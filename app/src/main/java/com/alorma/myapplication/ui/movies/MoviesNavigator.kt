package com.alorma.myapplication.ui.movies

import android.app.Activity
import com.alorma.myapplication.ui.SearchActivity
import com.alorma.myapplication.ui.common.Navigator
import com.alorma.myapplication.ui.detail.MovieDetailActivity

class MoviesNavigator(activity: Activity) : Navigator<MoviesRoutes.MovieRoute>(activity) {

    override fun navigate(route: MoviesRoutes.MovieRoute) {
        when (route) {
            is MoviesRoutes.MovieRoute.DetailRoute -> openDetail(route)
            MoviesRoutes.MovieRoute.Search -> openSearch()
        }
    }

    private fun openDetail(route: MoviesRoutes.MovieRoute.DetailRoute)
            = start(MovieDetailActivity.launch(activity, route.id, route.title))

    private fun openSearch() = start(SearchActivity.launch(activity))
}