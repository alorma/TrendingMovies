package com.alorma.myapplication.ui.common

import android.content.Context
import android.support.annotation.StringRes
import javax.inject.Inject

class ResourcesProvider @Inject constructor(private val context: Context) {
    fun getString(@StringRes stringId: Int, vararg arg: Any): String = context.resources.getString(stringId, *arg)
}