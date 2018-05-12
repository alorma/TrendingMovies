package com.alorma.myapplication.ui.detail

import com.alorma.myapplication.commons.observeOnUI
import com.alorma.myapplication.domain.model.Configuration
import com.alorma.myapplication.domain.model.TvShow
import com.alorma.myapplication.domain.usecase.ObtainConfigurationUseCase
import com.alorma.myapplication.domain.usecase.ObtainShowDetailUseCase
import com.alorma.myapplication.domain.usecase.ObtainSimilarShowsUseCase
import com.alorma.myapplication.ui.common.BasePresenter
import com.alorma.rac1.commons.plusAssign
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import javax.inject.Inject

class ShowDetailPresenter @Inject constructor(
        private val detailStates: DetailStates,
        private val detailRoutes: DetailRoutes,
        private val detailNavigator: DetailNavigator,
        private val obtainShowDetailUseCase: ObtainShowDetailUseCase,
        private val obtainConfigurationUseCase: ObtainConfigurationUseCase,
        private val obtainSimilarShowsUseCase: ObtainSimilarShowsUseCase
) :
        BasePresenter<DetailActions.DetailAction, DetailStates.DetailState>() {

    private var id: Int = -1

    override fun reduce(action: DetailActions.DetailAction) {
        when (action) {
            is DetailActions.DetailAction.Load -> load(action).also {
                this.id = action.id
            }
            DetailActions.DetailAction.LoadSimilarPage -> loadSimilarShows(id, action)
            DetailActions.DetailAction.Back -> detailNavigator navigate detailRoutes.back()
            is DetailActions.DetailAction.OpenShow ->
                detailNavigator navigate detailRoutes.detail(action.id, action.text)
        }
    }

    private fun load(action: DetailActions.DetailAction.Load) {
        loadShow(action.id)
        loadSimilarShows(action.id, action)
    }

    private fun loadShow(id: Int) {
        disposable += Single.zip(
                obtainConfigurationUseCase.execute(),
                obtainShowDetailUseCase.execute(id),
                BiFunction<Configuration, TvShow, Pair<Configuration, TvShow>> { conf, show ->
                    conf to show
                })
                .observeOnUI()
                .subscribe(
                        { render(detailStates success it) },
                        { render(detailStates error it) }
                )
    }

    private fun loadSimilarShows(id: Int, action: DetailActions.DetailAction) {
        disposable += Single.zip(
                obtainConfigurationUseCase.execute(),
                getSimilarShows(id, action),
                BiFunction<Configuration, List<TvShow>, Pair<Configuration, List<TvShow>>> { conf, shows ->
                    conf to shows
                })
                .observeOnUI()
                .subscribe(
                        { render(detailStates successSimilarShows it) },
                        { render(detailStates errorSimilarShows it) }
                )
    }

    private fun getSimilarShows(id: Int, action: DetailActions.DetailAction) =
            when {
                action is DetailActions.DetailAction.Load -> obtainSimilarShowsUseCase.execute(id)
                action === DetailActions.DetailAction.LoadSimilarPage -> obtainSimilarShowsUseCase.executeNextPage(id)
                else -> Single.never()
            }
}
