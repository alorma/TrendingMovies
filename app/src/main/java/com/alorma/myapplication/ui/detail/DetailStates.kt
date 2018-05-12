package com.alorma.myapplication.ui.detail

import com.alorma.myapplication.domain.model.Configuration
import com.alorma.myapplication.domain.model.TvShow
import com.alorma.myapplication.ui.common.State
import javax.inject.Inject

class DetailStates @Inject constructor(private val mapper: DetailMapper) {
    sealed class DetailState : State() {
        data class Success(val detail: TvShowDetailVm) : DetailState()
        data class SimilarShows(val shows: List<TvShowDetailVm>) : DetailState()
        data class Error(val text: String) : DetailState()
        data class ErrorSimilarShows(val text: String) : DetailState()
    }

    infix fun success(it: Pair<Configuration, TvShow>): DetailState =
            DetailState.Success(mapper.success(it.second, it.first))

    infix fun successSimilarShows(items: Pair<Configuration, List<TvShow>>): DetailState =
            DetailState.SimilarShows(items.second.map { mapper.success(it, items.first) })

    infix fun error(it: Throwable): DetailState =
            DetailState.Error(mapper mapError it)

    infix fun errorSimilarShows(it: Throwable): DetailState =
            DetailState.ErrorSimilarShows(mapper mapError it)
}