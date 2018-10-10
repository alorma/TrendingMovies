package com.alorma.myapplication.ui.common

import android.content.Context
import androidx.annotation.StringRes
import com.alorma.domain.common.ResourceProvider

class ResourcesProviderImpl(private val context: Context) : ResourceProvider {
    override fun getString(@StringRes stringId: Int, vararg arg: Any): String = context.resources.getString(stringId, *arg)
}