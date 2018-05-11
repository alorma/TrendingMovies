package com.alorma.myapplication.data.net

import com.google.gson.annotations.SerializedName

data class PagedResponse<out M>(@SerializedName("page") val page: Int,
                                @SerializedName("total_pages") val totalPages: Int,
                                @SerializedName("results") val results: List<M>)