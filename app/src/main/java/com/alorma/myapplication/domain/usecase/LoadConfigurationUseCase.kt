package com.alorma.myapplication.domain.usecase

import com.alorma.myapplication.domain.repository.ConfigurationRepository
import io.reactivex.Completable
import javax.inject.Inject

class LoadConfigurationUseCase @Inject constructor(
        private val configurationRepository: ConfigurationRepository) {

    fun execute(): Completable = configurationRepository.getConfig().toCompletable()
}