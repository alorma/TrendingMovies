package com.alorma.myapplication.domain.repository

import android.arch.paging.PagedList
import com.alorma.myapplication.commons.observeOnUI
import com.alorma.myapplication.commons.subscribeOnIO
import com.alorma.myapplication.domain.model.Movie
import javax.inject.Inject
import com.alorma.myapplication.data.cache.MoviesDataSource as Cache
import com.alorma.myapplication.data.net.MoviesDataSource as Network

class PageListMovieBoundaryCallback @Inject constructor(
        private val network: Network,
        private val cache: Cache) : PagedList.BoundaryCallback<Movie>() {

    private var isRequestRunning = false
    private var requestedPage = 1

    override fun onZeroItemsLoaded() {
        fetchAndStoreMovies()
    }

    override fun onItemAtEndLoaded(itemAtEnd: Movie) {
        fetchAndStoreMovies()
    }

    private fun fetchAndStoreMovies() {
        if (isRequestRunning) return

        isRequestRunning = true
        network.listAll(requestedPage)
                .doOnSuccess {
                    cache.save(it)
                    requestedPage++
                }
                .subscribeOnIO()
                .observeOnUI()
                .toCompletable()
                .doFinally { isRequestRunning = false }
                .subscribe(
                        { },
                        { it.printStackTrace() }
                )

    }
}
