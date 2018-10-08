package com.alorma.myapplication.common

import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers

fun Completable.observeOnUI() = with(this) {
    observeOn(AndroidSchedulers.mainThread())
}

fun <T> Flowable<T>.observeOnUI() = with(this) {
    observeOn(AndroidSchedulers.mainThread())
}

fun <T> Observable<T>.observeOnUI() = with(this) {
    observeOn(AndroidSchedulers.mainThread())
}

fun <T> Single<T>.observeOnUI() = with(this) {
    observeOn(AndroidSchedulers.mainThread())
}

fun <T> Maybe<T>.observeOnUI() = with(this) {
    observeOn(AndroidSchedulers.mainThread())
}