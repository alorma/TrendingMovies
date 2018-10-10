package com.alorma.domain.usecase

import com.alorma.domain.model.Configuration
import com.alorma.domain.repository.ConfigurationRepository
import io.reactivex.Single

class ObtainConfigurationUseCase(private val configurationRepository: ConfigurationRepository) {

    fun execute(): Single<Configuration> = configurationRepository.getConfig()
}