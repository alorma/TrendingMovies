package com.alorma.data.common

import io.reactivex.*
import io.reactivex.schedulers.Schedulers

fun Completable.subscribeOnIO() = with(this) {
    subscribeOn(Schedulers.io())
}

fun <T> Flowable<T>.subscribeOnIO() = with(this) {
    subscribeOn(Schedulers.io())
}

fun <T> Observable<T>.subscribeOnIO() = with(this) {
    subscribeOn(Schedulers.io())
}

fun <T> Single<T>.subscribeOnIO() = with(this) {
    subscribeOn(Schedulers.io())
}

fun <T> Maybe<T>.subscribeOnIO() = with(this) {
    subscribeOn(Schedulers.io())
}
