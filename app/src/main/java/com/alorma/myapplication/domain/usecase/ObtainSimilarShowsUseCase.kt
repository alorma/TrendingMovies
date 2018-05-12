package com.alorma.myapplication.domain.usecase

import com.alorma.myapplication.domain.model.TvShow
import com.alorma.myapplication.domain.repository.ShowsRepository
import io.reactivex.Single
import javax.inject.Inject

class ObtainSimilarShowsUseCase @Inject constructor(
        private val showsRepository: ShowsRepository) {

    fun execute(id: Int): Single<List<TvShow>> = showsRepository.similar(id)

    fun executeNextPage(id: Int): Single<List<TvShow>> = showsRepository.similarPage(id)
}