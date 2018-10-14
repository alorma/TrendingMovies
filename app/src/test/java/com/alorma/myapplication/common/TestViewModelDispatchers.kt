package com.alorma.myapplication.common

import com.alorma.presentation.common.ViewModelDispatchers
import kotlinx.coroutines.experimental.Dispatchers
import kotlin.coroutines.experimental.CoroutineContext

class TestViewModelDispatchers : ViewModelDispatchers() {
    override val main: CoroutineContext
        get() = Dispatchers.Unconfined
}