package com.alorma.myapplication.ui.common

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.createPagination(offset: Int = 4, function: () -> Unit): RecyclerView.OnScrollListener {
    return object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            (recyclerView.layoutManager as? LinearLayoutManager)?.apply {
                val firstVisibleItemPosition = findFirstVisibleItemPosition()
                val last = childCount + firstVisibleItemPosition
                if (last >= itemCount - offset) {
                    function()
                }
            }
        }
    }
}