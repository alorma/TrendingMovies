package com.alorma.myapplication.ui.common

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import io.reactivex.disposables.CompositeDisposable

open class Action
open class Route
open class State

abstract class BasePresenter<in A : Action, S : State>(private val liveData: MutableLiveData<S>) {

    protected val disposable: CompositeDisposable by lazy { CompositeDisposable() }

    fun init(): LiveData<S> = liveData

    abstract infix fun reduce(action: A)

    fun render(state: S) = liveData.postValue(state)

    fun destroy() {
        disposable.clear()
    }
}