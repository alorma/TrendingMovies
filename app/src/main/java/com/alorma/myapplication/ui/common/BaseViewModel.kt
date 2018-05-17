package com.alorma.myapplication.ui.common

import android.arch.lifecycle.*
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel<T : State, in A : Action> : ViewModel() {
    private val liveData: MutableLiveData<T> = MutableLiveData()

    protected val disposable: CompositeDisposable by lazy { CompositeDisposable() }

    fun init(owner: LifecycleOwner, observer: Observer<T>): LiveData<T> = liveData.also {
        liveData.observe(owner, observer)
    }

    abstract infix fun reduce(action: A)

    open fun render(state: T) = liveData.postValue(state)

    override fun onCleared() {
        disposable.clear()
        super.onCleared()
    }
}