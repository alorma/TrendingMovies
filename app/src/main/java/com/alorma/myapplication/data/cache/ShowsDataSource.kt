package com.alorma.myapplication.data.cache

import com.alorma.myapplication.domain.model.TvShow

class ShowsDataSource {

    private val allItems: MutableList<TvShow> = mutableListOf()

    private val pageSimilarShowsMap: MutableMap<Int, Int> = mutableMapOf()

    private val items: MutableList<TvShow> = mutableListOf()

    private val similarShowsMap: MutableMap<Int, MutableList<TvShow>> = mutableMapOf()

    fun clear() {
        items.clear()
    }

    fun save(items: List<TvShow>) {
        this.items.addAll(items)
        this.allItems.addAll(items)
    }

    fun get(): List<TvShow> = items.toList()

    fun get(id: Int): TvShow? = allItems.firstOrNull { it.id == id }

    fun clearSimilar(id: Int) {
        similarShowsMap[id]?.clear()
    }

    fun getSimilar(id: Int): List<TvShow> = similarShowsMap[id]?.toList() ?: listOf()

    fun saveSimilar(id: Int, items: List<TvShow>) {
        if (similarShowsMap.containsKey(id).not()) {
            similarShowsMap[id] = mutableListOf()
        }
        this.similarShowsMap[id]?.addAll(items)
        this.allItems.addAll(items)
    }

    fun setPageSimilarShows(id: Int, it: Int) {
        this.pageSimilarShowsMap[id] = it
    }

    fun getSimilarShowPage(id: Int): Int = pageSimilarShowsMap[id] ?: 0
}
