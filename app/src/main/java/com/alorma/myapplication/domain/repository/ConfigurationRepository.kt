package com.alorma.myapplication.domain.repository

import com.alorma.myapplication.domain.model.Configuration
import io.reactivex.Single

interface ConfigurationRepository {

    fun getConfig(): Single<Configuration>

}