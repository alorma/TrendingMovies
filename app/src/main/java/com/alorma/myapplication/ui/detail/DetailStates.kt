package com.alorma.myapplication.ui.detail

import com.alorma.myapplication.ui.common.State
import javax.inject.Inject

class DetailStates @Inject constructor() {
    sealed class DetailState : State() {

    }
}