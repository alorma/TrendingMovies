package com.alorma.myapplication.ui.common

import io.reactivex.disposables.CompositeDisposable

open class Action
open class Route
open class State

interface BaseView<in S : State> {
    infix fun render(state: S)
}

abstract class BasePresenter<in A : Action, S : State> {

    protected val disposable: CompositeDisposable by lazy { CompositeDisposable() }

    lateinit var view: BaseView<S>

    infix fun init(view: BaseView<S>) {
        this.view = view
    }

    abstract infix fun reduce(action: A)

    fun render(state: S) = view render state

    fun destroy() {
        disposable.clear()
    }
}