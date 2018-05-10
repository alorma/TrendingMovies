package com.alorma.myapplication.ui.common

import io.reactivex.disposables.CompositeDisposable

open class Action
open class Route
open class State

interface BaseView<in S : State, in R : Route> {
    infix fun render(state: S)
    infix fun navigate(route: R)
}

abstract class BasePresenter<in A : Action, S : State, R : Route> {

    protected val disposable: CompositeDisposable by lazy { CompositeDisposable() }

    lateinit var view: BaseView<S, R>

    infix fun init(view: BaseView<S, R>) {
        this.view = view
    }

    abstract infix fun reduce(action: A)

    fun render(state: S) = view render state

    fun navigate(route: R) = view navigate route

    fun destroy() {
        disposable.clear()
    }
}