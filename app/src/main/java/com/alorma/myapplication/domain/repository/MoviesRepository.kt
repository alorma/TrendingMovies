package com.alorma.myapplication.domain.repository

import android.arch.paging.PagedList
import android.arch.paging.RxPagedListBuilder
import com.alorma.myapplication.domain.model.Movie
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import com.alorma.myapplication.data.cache.MoviesDataSource as Cache
import com.alorma.myapplication.data.net.MoviesDataSource as Network

class MoviesRepository(private val cache: Cache,
                       private val callback: PagedList.BoundaryCallback<Movie>) {

    fun listAll(): Observable<PagedList<Movie>> = RxPagedListBuilder(cache.get(), 20)
            .setFetchScheduler(Schedulers.io())
            .setBoundaryCallback(callback)
            .buildObservable()

    fun search(query: String): Single<List<Movie>> = Single.never()

    fun searchNextPage(query: String): Single<List<Movie>> = Single.never()

    fun similar(id: Int): Single<List<Movie>> = Single.never()

    fun similarPage(id: Int): Single<List<Movie>> = Single.never()

    private fun executeSimilar(id: Int, operation: Single<Triple<Int, Int, List<Movie>>>):
            Single<List<Movie>> = Single.never()

    fun getMovie(id: Int): Single<Movie> = Single.never()
}