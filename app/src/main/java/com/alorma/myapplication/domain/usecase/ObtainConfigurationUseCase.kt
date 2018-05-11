package com.alorma.myapplication.domain.usecase

import com.alorma.myapplication.domain.model.Configuration
import com.alorma.myapplication.domain.repository.ConfigurationRepository
import io.reactivex.Single
import javax.inject.Inject

class ObtainConfigurationUseCase @Inject constructor(
        private val configurationRepository: ConfigurationRepository) {

    fun execute(): Single<Configuration> = configurationRepository.getConfig()
}