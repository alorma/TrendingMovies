package com.alorma.myapplication.ui.common

import io.reactivex.disposables.CompositeDisposable

open class Action
open class Route
open class State

interface BaseView<in R : Route, in S : State> {
    infix fun render(s: S)
    infix fun navigate(r: R)
}

abstract class BasePresenter<in A : Action, S : State, R : Route> {

    protected val disposable: CompositeDisposable by lazy { CompositeDisposable() }

    lateinit var view: BaseView<R, S>

    infix fun init(view: BaseView<R, S>) {
        this.view = view
    }

    abstract infix fun reduce(action: A)

    fun render(state: S) = view render state

    fun navigate(route: R) = view navigate route

    fun destroy() {
        disposable.clear()
    }
}