package com.alorma.myapplication.ui.detail

import com.alorma.myapplication.domain.model.TvShow
import com.alorma.myapplication.ui.common.State
import com.alorma.myapplication.ui.shows.ShowsStates
import javax.inject.Inject

class DetailStates @Inject constructor(private val mapper: DetailMapper) {
    sealed class DetailState : State() {
        data class Success(val id: Int) : DetailState()
        data class Error(val text: String) : DetailState()
    }

    infix fun success(it: TvShow): DetailState = DetailState.Success(it.id)

    infix fun error(it: Throwable): DetailState.Error = DetailState.Error(mapper mapError it)
}