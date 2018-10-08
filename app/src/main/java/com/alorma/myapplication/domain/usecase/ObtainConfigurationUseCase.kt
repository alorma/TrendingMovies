package com.alorma.myapplication.domain.usecase

import com.alorma.myapplication.domain.model.Configuration
import com.alorma.myapplication.domain.repository.ConfigurationRepository
import io.reactivex.Single

class ObtainConfigurationUseCase(private val configurationRepository: ConfigurationRepository) {

    fun execute(): Single<Configuration> = configurationRepository.getConfig()
}