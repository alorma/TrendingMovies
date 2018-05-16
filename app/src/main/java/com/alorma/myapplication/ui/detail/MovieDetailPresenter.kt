package com.alorma.myapplication.ui.detail

import android.arch.lifecycle.MutableLiveData
import com.alorma.myapplication.commons.observeOnUI
import com.alorma.myapplication.domain.model.Configuration
import com.alorma.myapplication.domain.model.Movie
import com.alorma.myapplication.domain.usecase.ObtainConfigurationUseCase
import com.alorma.myapplication.domain.usecase.ObtainMovieDetailUseCase
import com.alorma.myapplication.domain.usecase.ObtainMovieUseCase
import com.alorma.myapplication.ui.common.BasePresenter
import com.alorma.rac1.commons.plusAssign
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import javax.inject.Inject

class MovieDetailPresenter @Inject constructor(
        private val detailStates: DetailStates,
        private val detailRoutes: DetailRoutes,
        private val detailNavigator: DetailNavigator,
        private val obtainMovieDetailUseCase: ObtainMovieDetailUseCase,
        private val obtainConfigurationUseCase: ObtainConfigurationUseCase,
        private val obtainMovieUseCase: ObtainMovieUseCase,
        liveData: MutableLiveData<DetailStates.DetailState>
) :
        BasePresenter<DetailActions.DetailAction, DetailStates.DetailState>(liveData) {

    private var id: Int = -1

    override fun reduce(action: DetailActions.DetailAction) {
        when (action) {
            is DetailActions.DetailAction.Load -> load(action).also {
                this.id = action.id
            }
            DetailActions.DetailAction.LoadSimilarPage -> loadSimilarMovies(id, action)
            DetailActions.DetailAction.Back -> detailNavigator navigate detailRoutes.back()
            is DetailActions.DetailAction.OpenMovie ->
                detailNavigator navigate detailRoutes.detail(action.id, action.text)
        }
    }

    private fun load(action: DetailActions.DetailAction.Load) {
        loadMovie(action.id)
        loadSimilarMovies(action.id, action)
    }

    private fun loadMovie(id: Int) {
        disposable += Single.zip(
                obtainConfigurationUseCase.execute(),
                obtainMovieDetailUseCase.execute(id),
                BiFunction<Configuration, Movie, Pair<Configuration, Movie>> { conf, movie ->
                    conf to movie
                })
                .observeOnUI()
                .subscribe(
                        { render(detailStates success it) },
                        { render(detailStates error it) }
                )
    }

    private fun loadSimilarMovies(id: Int, action: DetailActions.DetailAction) {
        disposable += Single.zip(
                obtainConfigurationUseCase.execute(),
                getSimilarMovies(id, action),
                BiFunction<Configuration, List<Movie>, Pair<Configuration, List<Movie>>> { conf, movie ->
                    conf to movie
                })
                .observeOnUI()
                .subscribe(
                        { render(detailStates successSimilarMovies it) },
                        {
                            render(detailStates errorSimilarMovies it)
                        }
                )
    }

    private fun getSimilarMovies(id: Int, action: DetailActions.DetailAction) =
            when {
                action is DetailActions.DetailAction.Load -> obtainMovieUseCase.execute(id)
                action === DetailActions.DetailAction.LoadSimilarPage -> obtainMovieUseCase.executeNextPage(id)
                else -> Single.never()
            }
}
