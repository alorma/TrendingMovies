package com.alorma.myapplication.ui.shows

import com.alorma.myapplication.ui.common.Action
import javax.inject.Inject

class ShowsActions @Inject constructor() {
    sealed class ShowsAction : Action() {
        object Load : ShowsAction()
        object LoadPage : ShowsAction()
        data class OpenDetail(val id: Int, val title: String) : ShowsAction()
    }

    fun load(): ShowsAction = ShowsAction.Load
    fun loadPage(): ShowsAction = ShowsAction.LoadPage
    fun detail(item: TvShowVM): ShowsAction = ShowsAction.OpenDetail(item.id, item.title)
}