package com.alorma.presentation.common

import androidx.lifecycle.*
import kotlinx.coroutines.experimental.*

abstract class BaseViewModel<S : State, R : Route, A : Action, E : Event>(
        private val dispatcher: ViewModelDispatchers
) : ViewModel() {

    val state: LiveData<S>
        get() = stateLiveData
    val route: LiveData<R>
        get() = routeLiveData
    val event: LiveData<EventHandler<E>>
        get() = eventLiveData

    private val stateLiveData: MutableLiveData<S> = MutableLiveData()
    private val routeLiveData: MutableLiveData<R> = MutableLiveData()
    private val eventLiveData: MutableLiveData<EventHandler<E>> = MutableLiveData()

    private val parentJob: Job = Job()
    private val jobs: MutableList<Job> = mutableListOf()

    abstract infix fun reduce(action: A)

    protected fun render(s: S) {
        stateLiveData.postValue(s)
    }

    protected fun navigate(r: R) {
        routeLiveData.postValue(r)
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

    fun launch(handler: ErrorHandler? = null,
               block: suspend CoroutineScope.() -> Unit) {

        val errorHandler = CoroutineExceptionHandler { _, exception ->
            handler?.onError(exception)
        }

        val job = GlobalScope.launch(dispatcher.main.plus(errorHandler)) {
            block()
        }
        addJob(job)
    }

    interface ErrorHandler {
        fun onError(exception: Throwable)
    }

    private fun addJob(job: Job) {
        jobs.add(job)
    }

    fun observe(lifecycleOwner: LifecycleOwner, dsl: ViewModelObserver<S, R, A, E>.() -> Unit) {
        ViewModelObserver(lifecycleOwner, this).apply(dsl).build()
    }

    override fun onCleared() {
        clear()
    }

    protected fun clear() {
        parentJob.cancel()
    }
}

@DslMarker
annotation class ViewModelDsl

@ViewModelDsl
class ViewModelObserver<S : State, R : Route, A : Action, E : Event>(private val lifecycleOwner: LifecycleOwner,
                                                                     private val vm: BaseViewModel<S, R, A, E>) {

    private lateinit var stateBlock: (S) -> Unit
    private lateinit var routeBlock: (R) -> Unit
    private lateinit var eventBlock: (E) -> Unit

    fun build() {
        vm.state.observe(lifecycleOwner, Observer { it?.let(stateBlock) })
        vm.route.observe(lifecycleOwner, Observer { it?.let(routeBlock) })
        vm.event.observe(lifecycleOwner, Observer { it?.peekContent()?.let(eventBlock) })
    }

    fun onState(block: (S) -> Unit) {
        this.stateBlock = block
    }

    fun onRoute(block: (R) -> Unit) {
        this.routeBlock = block
    }

    fun onEvent(block: (E) -> Unit) {
        this.eventBlock = block
    }
}