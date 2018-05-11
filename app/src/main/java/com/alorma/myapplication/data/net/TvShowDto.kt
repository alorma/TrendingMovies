package com.alorma.myapplication.data.net

import com.google.gson.annotations.SerializedName

data class TvShowDto(@SerializedName("id") val id: Int,
                     @SerializedName("name") val title: String,
                     @SerializedName("backdrop_path") val backdropImage: String,
                     @SerializedName("poster_path") val posterImage: String,
                     @SerializedName("vote_average") val vote: Float)