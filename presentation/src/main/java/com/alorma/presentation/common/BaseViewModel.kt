package com.alorma.presentation.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
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

    fun observe(lifecycleOwner: ViewModelObserver<S, R, E>) {
        observe(lifecycleOwner, lifecycleOwner, lifecycleOwner)
    }

    fun observe(stateOwner: ViewModelState<S>? = null,
                routeOwner: ViewModelRoute<R>? = null,
                eventOwner: ViewModelEvent<E>? = null) {
        stateOwner?.let { stateViewModelObserver ->
            state.observe(stateViewModelObserver, Observer { stateValue ->
                stateValue?.let { stateHandledValue -> stateViewModelObserver.onState(stateHandledValue) }
            })
        }
        routeOwner?.let { routeViewModelObserver ->
            route.observe(routeViewModelObserver, Observer { routeValue ->
                routeValue.let { routeHandledValue -> routeViewModelObserver.onRoute(routeHandledValue) }
            })
        }

        eventOwner?.let { eventViewObserver ->
            event.observe(eventViewObserver, Observer { eventValue ->
                eventValue?.peekContent()?.let { eventHandledValue -> eventViewObserver.onEvent(eventHandledValue) }
            })
        }
    }

    override fun onCleared() {
        clear()
    }

    protected fun clear() {
        parentJob.cancel()
    }
}