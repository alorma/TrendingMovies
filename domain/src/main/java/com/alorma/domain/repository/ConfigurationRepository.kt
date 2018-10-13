package com.alorma.domain.repository

import com.alorma.domain.model.Configuration

interface ConfigurationRepository {

    suspend fun getConfig(): Configuration

}