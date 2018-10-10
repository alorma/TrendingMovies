package com.alorma.domain.model

data class Configuration(val imagesUrl: String, val imageSize: String, val posterSize: String,
                         val genres: List<Pair<Int, String>>)