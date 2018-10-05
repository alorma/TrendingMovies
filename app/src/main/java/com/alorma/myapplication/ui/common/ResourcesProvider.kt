package com.alorma.myapplication.ui.common

import android.content.Context
import androidx.annotation.StringRes

class ResourcesProvider(private val context: Context) {
    fun getString(@StringRes stringId: Int, vararg arg: Any): String = context.resources.getString(stringId, *arg)
}