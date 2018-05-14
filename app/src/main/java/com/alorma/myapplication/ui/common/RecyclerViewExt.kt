package com.alorma.myapplication.ui.common

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.alorma.myapplication.ui.movies.MoviesActivity

fun RecyclerView.pagination(function: () -> Unit): RecyclerView.OnScrollListener {
    return object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            (recyclerView.layoutManager as? LinearLayoutManager)?.apply {
                val firstVisibleItemPosition = findFirstVisibleItemPosition()
                val last = childCount + firstVisibleItemPosition
                if (last >= itemCount - MoviesActivity.OFFSET_LAZY_LOAD) {
                    function()
                }
            }
        }
    }
}