package com.alorma.presentation.common

import kotlinx.coroutines.experimental.Dispatchers
import kotlin.coroutines.experimental.CoroutineContext

open class ViewModelDispatchers {
    open val main: CoroutineContext = Dispatchers.Main
}