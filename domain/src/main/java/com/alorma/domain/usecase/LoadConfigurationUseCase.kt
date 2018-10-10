package com.alorma.domain.usecase

import com.alorma.domain.repository.ConfigurationRepository
import io.reactivex.Completable

class LoadConfigurationUseCase(private val configurationRepository: ConfigurationRepository) {

    fun execute(): Completable = configurationRepository.getConfig().ignoreElement()
}