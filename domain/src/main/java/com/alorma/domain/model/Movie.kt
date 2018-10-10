package com.alorma.domain.model

import java.util.*

data class Movie(val id: Int, val title: String, val overview: String?,
                 val images: Images, val date: Date?, val vote: Float, val genres: List<Int>)

data class Images(val poster: String?, val backdrop: String?)