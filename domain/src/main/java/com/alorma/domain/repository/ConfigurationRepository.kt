package com.alorma.domain.repository

import com.alorma.domain.model.Configuration
import io.reactivex.Single

interface ConfigurationRepository {

    fun getConfig(): Single<Configuration>

}