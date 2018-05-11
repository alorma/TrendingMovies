package com.alorma.myapplication.data.cache

import com.alorma.myapplication.domain.model.TvShow
import javax.inject.Inject

class ShowsDataSource @Inject constructor() {

    private val items: MutableList<TvShow> = mutableListOf()

    fun clear() {
        items.clear()
    }

    fun save(items: List<TvShow>) {
        this.items.addAll(items)
    }

    fun get(): List<TvShow> = items.toList()
}