package com.alorma.presentation.common

import androidx.lifecycle.LifecycleOwner

interface ViewModelObserver<S : State, R : Route, E : Event> :
        ViewModelState<S>, ViewModelRoute<R>, ViewModelEvent<E>

interface ViewModelState<S : State> : LifecycleOwner {
    fun onState(state: S)
}

interface ViewModelRoute<R : Route> : LifecycleOwner {
    fun onRoute(route: R)
}

interface ViewModelEvent<E : Event> : LifecycleOwner {
    fun onEvent(event: E)
}
