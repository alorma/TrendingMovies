package com.alorma.myapplication.domain.model

data class Configuration(val imagesUrl: String, val imageSize: String, val posterSize: String,
                         val genres: List<Pair<Int, String>>)