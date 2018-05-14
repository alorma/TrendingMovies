package com.alorma.myapplication.ui.movies

import com.alorma.myapplication.R
import com.alorma.myapplication.ui.common.ResourcesProvider
import javax.inject.Inject

class MoviesMapper @Inject constructor(private val resources: ResourcesProvider) {

    infix fun mapError(it: Throwable): String = resources.getString(R.string.generic_error)

}