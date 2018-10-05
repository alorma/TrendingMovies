package com.alorma.myapplication.ui.common

import android.content.Context
import androidx.annotation.StringRes
import javax.inject.Inject

class ResourcesProvider @Inject constructor(private val context: Context) {
    fun getString(@StringRes stringId: Int, vararg arg: Any): String = context.resources.getString(stringId, *arg)
    fun getText(@StringRes stringId: Int): CharSequence = context.resources.getText(stringId)
}