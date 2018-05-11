package com.alorma.myapplication.data.cache

import com.alorma.myapplication.domain.model.Configuration

class ConfigDataSource {
    private var config: Configuration? = null

    fun save(it: Configuration) {
        this.config = it
    }

    fun get(): Configuration? = config
}