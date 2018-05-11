package com.alorma.myapplication.domain.usecase

import com.alorma.myapplication.domain.model.TvShow
import com.alorma.myapplication.domain.repository.ShowsRepository
import io.reactivex.Single
import javax.inject.Inject

class ObtainShowDetailUseCase @Inject constructor(private val showsRepository: ShowsRepository) {
    fun execute(id: Int): Single<TvShow> = showsRepository.getShow(id)
}