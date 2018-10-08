package com.alorma.myapplication.data.cache

import com.alorma.domain.model.Configuration

class LocalConfigDataSource {
    private var config: Configuration? = null

    fun save(it: Configuration) {
        this.config = it
    }

    fun get(): Configuration? = config
}