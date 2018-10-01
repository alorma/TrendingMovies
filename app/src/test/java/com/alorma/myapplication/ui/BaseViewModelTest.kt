package com.alorma.myapplication.ui

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import assertk.assert
import assertk.assertions.isNotNull
import com.alorma.myapplication.configureRxThreading
import com.alorma.myapplication.ui.common.*
import com.nhaarman.mockito_kotlin.KArgumentCaptor
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestRule

abstract class BaseViewModelTest<S : State, R : Route, A : Action, E : Event, VM : BaseViewModel<S, R, A, E>> {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val navigator: Navigator<R> = mock()
    private val stateObserver: Observer<S> = mock()
    private val eventObserver: Observer<EventHandler<E>> = mock()

    protected val stateCaptor: KArgumentCaptor<S> by lazy { createStateCaptor() }
    protected val routeCaptor: KArgumentCaptor<R> by lazy { createRouteCaptor() }
    protected val eventCaptor: KArgumentCaptor<EventHandler<E>> by lazy { createEventCaptor() }

    protected lateinit var vm: VM

    init {
        configureRxThreading()
    }

    @Before
    fun setup() {
        vm = createViewModel(navigator).apply {
            state.observeForever(stateObserver)
            event.observeForever(eventObserver)
            addObservers()
        }
    }

    protected open fun VM.addObservers() {

    }

    abstract fun createViewModel(navigator: Navigator<R>): VM
    abstract fun createStateCaptor(): KArgumentCaptor<S>
    abstract fun createEventCaptor(): KArgumentCaptor<EventHandler<E>>
    abstract fun createRouteCaptor(): KArgumentCaptor<R>

    protected fun captureState(times: Int = 1) {
        if (times > 1) {
            verify(stateObserver, times(times))
        } else {
            verify(stateObserver)
        }.onChanged(stateCaptor.capture())
    }

    protected fun captureState(times: Int = 1, block: () -> A) {
        runAction(block())
        captureState(times)
    }

    protected fun captureEvent(times: Int = 1) {
        if (times > 1) {
            verify(eventObserver, times(times))
        } else {
            verify(eventObserver)
        }.onChanged(eventCaptor.capture())
    }

    protected fun captureEvent(times: Int = 1, block: () -> A) {
        runAction(block())
        captureEvent(times)
    }

    protected fun captureRoute() {
        verify(navigator).navigate(routeCaptor.capture())
    }

    protected inline fun captureRoute(block: () -> A) {
        runAction(block())
        captureRoute()
    }

    protected fun <Z> captureOne(observer: Observer<Z>,
                                 captor: KArgumentCaptor<Z>,
                                 block: () -> A) {
        runAction(block())
        captureAnyState(1, observer, captor)
    }

    protected fun <Z> captureAny(times: Int = 1,
                                 observer: Observer<Z>,
                                 captor: KArgumentCaptor<Z>,
                                 block: () -> A) {
        runAction(block())
        captureAnyState(times, observer, captor)
    }

    private fun <Z> captureAnyState(times: Int = 1, observer: Observer<Z>, captor: KArgumentCaptor<Z>) {
        if (times > 1) {
            verify(observer, times(times))
        } else {
            verify(observer)
        }.onChanged(captor.capture())
    }

    fun EventHandler<E>.assertEvent(block: (Event) -> Unit) {
        val value: E? = peekContent()
        assert(value).isNotNull()
        value?.let(block)
    }

    fun runAction(action: A) {
        vm reduce action
    }
}