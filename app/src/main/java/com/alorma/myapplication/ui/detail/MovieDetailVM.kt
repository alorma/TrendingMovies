package com.alorma.myapplication.ui.detail

data class MovieDetailVM(val id: Int, val title: String, val overView: String?,
                         val image: String?, val date: String, val vote: CharSequence,
                         val genres: List<String>)