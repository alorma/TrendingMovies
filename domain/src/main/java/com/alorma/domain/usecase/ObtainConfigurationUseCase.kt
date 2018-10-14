package com.alorma.domain.usecase

import com.alorma.domain.model.Configuration
import com.alorma.domain.repository.ConfigurationRepository

class ObtainConfigurationUseCase(private val configurationRepository: ConfigurationRepository) {

    suspend fun execute(): Configuration = configurationRepository.getConfig()
}