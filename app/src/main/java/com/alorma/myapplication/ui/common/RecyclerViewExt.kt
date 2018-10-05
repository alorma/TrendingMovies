package com.alorma.myapplication.ui.common

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

fun androidx.recyclerview.widget.RecyclerView.pagination(function: () -> Unit): androidx.recyclerview.widget.RecyclerView.OnScrollListener {
    return object : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: androidx.recyclerview.widget.RecyclerView, dx: Int, dy: Int) {
            (recyclerView.layoutManager as? androidx.recyclerview.widget.LinearLayoutManager)?.apply {
                val firstVisibleItemPosition = findFirstVisibleItemPosition()
                val last = childCount + firstVisibleItemPosition
                if (last >= itemCount - MoviesActivity.OFFSET_LAZY_LOAD) {
                    function()
                }
            }
        }
    }
}