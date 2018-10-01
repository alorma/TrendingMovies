package com.alorma.myapplication.ui.common

import android.arch.lifecycle.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseViewModel<S : State, R : Route, A : Action, E : Event>(
        private val navigator: Navigator<R>
) : ViewModel() {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    val state: LiveData<S>
        get() = stateLiveData
    val event: LiveData<EventHandler<E>>
        get() = eventLiveData

    private val stateLiveData: MutableLiveData<S> = MutableLiveData()
    private val eventLiveData: MutableLiveData<EventHandler<E>> = MutableLiveData()

    abstract infix fun reduce(action: A)

    protected fun render(s: S) {
        stateLiveData.postValue(s)
    }

    protected fun navigate(r: R) {
        navigator navigate r
    }

    @JvmOverloads
    protected fun post(e: E, persist: Boolean = false) {
        eventLiveData.postValue(if (persist) {
            PersistentEventHandler(e)
        } else {
            NonPersistentEventHandler(e)
        })
    }

    fun runFirstTimeOnly(block: () -> Unit) {
        if (state.value == null) {
            block()
        }
    }

    protected fun addDisposable(d: Disposable) {
        compositeDisposable.addAll(d)
    }

    fun observe(lifecycleOwner: LifecycleOwner, dsl: ViewModelObserver<S, R, A, E>.() -> Unit) {
        ViewModelObserver(lifecycleOwner, this).apply(dsl).build()
    }

    override fun onCleared() {
        compositeDisposable.clear()
    }

    protected fun clear() {
        compositeDisposable.clear()
    }
}

@DslMarker
annotation class ViewModelDsl

@ViewModelDsl
class ViewModelObserver<S : State, R : Route, A : Action, E : Event>(private val lifecycleOwner: LifecycleOwner,
                                                                     private val vm: BaseViewModel<S, R, A, E>) {

    private lateinit var stateBlock: (S) -> Unit
    private lateinit var eventBlock: (E) -> Unit

    fun build() {
        vm.state.observe(lifecycleOwner, Observer { it?.let(stateBlock) })
        vm.event.observe(lifecycleOwner, Observer { it?.peekContent()?.let(eventBlock) })
    }

    fun onState(block: (S) -> Unit) {
        this.stateBlock = block
    }

    fun onEvent(block: (E) -> Unit) {
        this.eventBlock = block
    }
}