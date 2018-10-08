package com.alorma.data.net.config

import com.google.gson.annotations.SerializedName

data class GenreDtoResponse(@SerializedName("genres") val results: List<GenreDto>)

data class GenreDto(@SerializedName("id") val id: Int, @SerializedName("name") val name: String)