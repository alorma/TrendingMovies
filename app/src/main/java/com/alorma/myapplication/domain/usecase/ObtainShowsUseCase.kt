package com.alorma.myapplication.domain.usecase

import com.alorma.myapplication.domain.model.TvShow
import com.alorma.myapplication.domain.repository.ShowsRepository
import io.reactivex.Single
import javax.inject.Inject

class ObtainShowsUseCase @Inject constructor(
        private val showsRepository: ShowsRepository) {

    fun execute(): Single<List<TvShow>> = showsRepository.listAll()
}