package com.alorma.myapplication.domain.usecase

import com.alorma.myapplication.domain.repository.ConfigurationRepository
import io.reactivex.Completable

class LoadConfigurationUseCase(private val configurationRepository: ConfigurationRepository) {

    fun execute(): Completable = configurationRepository.getConfig().ignoreElement()
}