package com.alorma.myapplication.domain.model

import java.util.*

data class TvShow(val id: Int, val title: String, val overview: String,
                  val images: Images, val date: Date?)

data class Images(val poster: String, val backdrop: String)