package com.alorma.myapplication.ui.search

import com.alorma.myapplication.ui.common.Route
import javax.inject.Inject

class SearchRoutes @Inject constructor() {
    sealed class SearchRoute : Route() {
        data class OpenDetail(val id: Int, val title: String) : SearchRoute()
    }

    infix fun detail(movie: MovieSearchItemVM): SearchRoute =
            SearchRoute.OpenDetail(movie.id, movie.title)

}