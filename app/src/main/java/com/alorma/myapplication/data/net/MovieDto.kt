package com.alorma.myapplication.data.net

import com.google.gson.annotations.SerializedName

data class MovieDto(@SerializedName("id") val id: Int,
                    @SerializedName("original_title") val title: String,
                    @SerializedName("overview") val overview: String,
                    @SerializedName("release_date") val releaseDate: String?,
                    @SerializedName("backdrop_path") val backdropImage: String,
                    @SerializedName("poster_path") val posterImage: String,
                    @SerializedName("vote_average") val vote: Float,
                    @SerializedName("genre_ids") val genres: List<Int>)