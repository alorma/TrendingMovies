package com.alorma.myapplication.ui.movies

import com.alorma.myapplication.ui.common.Route
import javax.inject.Inject

class MoviesRoutes @Inject constructor() {
    sealed class MovieRoute : Route() {
        data class DetailRoute(val id: Int, val title: String) : MovieRoute()
        object Search : MovieRoute()
    }

    fun detail(id: Int, title: String): MovieRoute = MovieRoute.DetailRoute(id, title)
    fun search(): MovieRoute = MovieRoute.Search
}