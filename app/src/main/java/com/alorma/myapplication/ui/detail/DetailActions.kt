package com.alorma.myapplication.ui.detail

import com.alorma.myapplication.ui.common.Action
import javax.inject.Inject

class DetailActions @Inject constructor() {
    sealed class DetailAction : Action() {
        data class Load(val id: Int) : DetailAction()
        object Back : DetailAction()
    }

    fun load(id: Int): DetailAction = DetailAction.Load(id)
    fun back(): DetailAction = DetailAction.Back
}