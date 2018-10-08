package com.alorma.data.cache

import com.alorma.domain.model.Movie

class LocalMoviesDataSource {

    private val allItems: MutableList<Movie> = mutableListOf()

    private val pageSimilarMoviesMap: MutableMap<Int, Int> = mutableMapOf()

    private val items: MutableList<Movie> = mutableListOf()
    private val itemsSearch: MutableList<Movie> = mutableListOf()

    private val similarMoviesMap: MutableMap<Int, MutableList<Movie>> = mutableMapOf()

    var page: Int = 0
    var searchPage: Int = 0

    fun clear() {
        items.clear()
    }

    fun clearSearch() {
        itemsSearch.clear()
    }

    fun save(items: List<Movie>) {
        this.items.addAll(items)
        this.allItems.addAll(items)
    }

    fun saveSearch(items: List<Movie>) {
        this.itemsSearch.addAll(items)
        this.allItems.addAll(items)
    }

    fun get(): List<Movie> = items.toList()

    fun getSearch(): List<Movie> = itemsSearch.toList()

    fun get(id: Int): Movie? = allItems.firstOrNull { it.id == id }

    fun clearSimilar(id: Int) {
        similarMoviesMap[id]?.clear()
    }

    fun getSimilar(id: Int): List<Movie> = similarMoviesMap[id]?.toList() ?: listOf()

    fun saveSimilar(id: Int, items: List<Movie>) {
        if (similarMoviesMap.containsKey(id).not()) {
            similarMoviesMap[id] = mutableListOf()
        }
        this.similarMoviesMap[id]?.addAll(items)
        this.allItems.addAll(items)
    }

    fun setPageSimilarMovies(id: Int, it: Int) {
        this.pageSimilarMoviesMap[id] = it
    }

    fun getSimilarMoviePage(id: Int): Int = pageSimilarMoviesMap[id] ?: 0
}
