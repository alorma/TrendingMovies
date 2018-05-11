package com.alorma.myapplication.ui.detail

import com.alorma.myapplication.R
import com.alorma.myapplication.domain.model.Configuration
import com.alorma.myapplication.domain.model.TvShow
import com.alorma.myapplication.ui.common.ResourcesProvider
import javax.inject.Inject

class DetailMapper @Inject constructor(private val resources: ResourcesProvider) {

    fun success(tvShow: TvShow, configuration: Configuration): TvShowDetailVm =
            TvShowDetailVm(tvShow.id, tvShow.title, tvShow.overview)

    infix fun mapError(it: Throwable): String = resources.getString(R.string.generic_error)
}