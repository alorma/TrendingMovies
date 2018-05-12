package com.alorma.myapplication.ui.detail

import com.alorma.myapplication.ui.common.Route
import javax.inject.Inject

class DetailRoutes @Inject constructor() {
    sealed class DetailRoute : Route() {
        data class Detail(val id: Int, val title: String) : DetailRoute()
        object Back : DetailRoute()
    }

    fun back(): DetailRoute = DetailRoute.Back
    fun detail(id: Int, title: String): DetailRoute = DetailRoute.Detail(id, title)
}