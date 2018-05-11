package com.alorma.myapplication.ui.shows

import com.alorma.myapplication.ui.common.Route
import javax.inject.Inject

class ShowsRoutes @Inject constructor() {
    sealed class ShowsRoute : Route() {
        data class DetailRoute(val id: Int, val title: String) : ShowsRoute()
    }

    fun detail(id: Int, title: String): ShowsRoute = ShowsRoute.DetailRoute(id, title)
}