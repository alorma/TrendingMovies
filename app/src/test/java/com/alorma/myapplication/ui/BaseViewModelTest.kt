package com.alorma.myapplication.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import assertk.assert
import assertk.assertions.isNotNull
import com.alorma.myapplication.common.TestViewModelDispatchers
import com.alorma.presentation.common.*
import com.nhaarman.mockito_kotlin.KArgumentCaptor
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestRule

abstract class BaseViewModelTest<S : State, R : Route, A : Action, E : Event> {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    protected lateinit var stateObserver: Observer<S>
    protected lateinit var routeObserver: Observer<R>
    protected lateinit var eventObserver: Observer<EventHandler<E>>

    protected lateinit var stateCaptor: KArgumentCaptor<S>
    protected lateinit var routeCaptor: KArgumentCaptor<R>
    protected lateinit var eventCaptor: KArgumentCaptor<EventHandler<E>>

    protected lateinit var vm: BaseViewModel<S, R, A, E>

    @Before
    fun setup() {
        stateCaptor = createStateCaptor()
        routeCaptor = createRouteCaptor()
        eventCaptor = createEventCaptor()

        stateObserver = mock()
        routeObserver = mock()
        eventObserver = mock()

        val dispatchers = TestViewModelDispatchers()

        vm = createViewModel(dispatchers).apply {
            state.observeForever(stateObserver)
            event.observeForever(eventObserver)
            addObservers()
        }
    }

    protected open fun BaseViewModel<S, R, A, E>.addObservers() {

    }

    abstract fun createViewModel(dispatchers: TestViewModelDispatchers): BaseViewModel<S, R, A, E>
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
        clearStates()
        runAction(block())
        captureState(times)
    }

    protected fun clearStates() {
        stateCaptor = createStateCaptor()
        vm.state.removeObserver(stateObserver)
        stateObserver = mock()
        vm.state.observeForever(stateObserver)
    }

    protected fun captureEvent(times: Int = 1) {
        if (times > 1) {
            verify(eventObserver, times(times))
        } else {
            verify(eventObserver)
        }.onChanged(eventCaptor.capture())
    }

    protected fun captureEvent(times: Int = 1, block: () -> A) {
        clearEvents()
        runAction(block())
        captureEvent(times)
    }

    protected fun clearEvents() {
        eventCaptor = createEventCaptor()
        vm.event.removeObserver(eventObserver)
        eventObserver = mock()
        vm.event.observeForever(eventObserver)
    }

    protected fun captureRoute(times: Int = 1) {
        if (times > 1) {
            verify(routeObserver, times(times))
        } else {
            verify(routeObserver)
        }.onChanged(routeCaptor.capture())
    }

    protected inline fun captureRoute(block: () -> A) {
        clearRoutes()
        runAction(block())
        captureRoute()
    }

    protected fun clearRoutes() {
        routeCaptor = createRouteCaptor()
        vm.route.removeObserver(routeObserver)
        routeObserver = mock()
        vm.route.observeForever(routeObserver)
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