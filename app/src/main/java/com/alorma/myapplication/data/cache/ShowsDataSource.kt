package com.alorma.myapplication.data.cache

import com.alorma.myapplication.domain.model.TvShow

class ShowsDataSource {

    private val items: MutableList<TvShow> = mutableListOf()

    fun clear() {
        items.clear()
    }

    fun save(items: List<TvShow>) {
        this.items.addAll(items)
    }

    fun get(): List<TvShow> = items.toList()

    fun get(id: Int): TvShow? = items.firstOrNull { it.id == id }
}