package com.alorma.myapplication.domain.repository

import com.alorma.myapplication.commons.subscribeOnIO
import com.alorma.myapplication.domain.model.Configuration
import io.reactivex.Single
import com.alorma.myapplication.data.net.ConfigDataSource as Network

class ConfigurationRepository(private val network: Network) {

    fun getConfig(): Single<Configuration> = network.get().subscribeOnIO()

}