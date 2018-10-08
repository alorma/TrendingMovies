package com.alorma.myapplication.ui.common

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

@DslMarker
annotation class DiffUtilsDsl

@DiffUtilsDsl
class DiffUtilsBuilder<R> {

    private lateinit var adapterDiff: AdapterDiff<R>
    lateinit var adapter: androidx.recyclerview.widget.RecyclerView.Adapter<*>
    lateinit var comparable: Comparator<R>
    var oldList: MutableList<R> = mutableListOf()
    var newList: List<R> = listOf()

    fun build() {
        adapterDiff = AdapterDiff(oldList, newList, comparable)
        adapterDiff.run { DiffUtil.calculateDiff(this) }
                .also {
                    this.oldList.apply {
                        clear()
                        addAll(newList)
                    }
                }
                .dispatchUpdatesTo(adapter)
    }
}

class AdapterDiff<R>(private val oldList: List<R>,
                     private val newList: List<R>,
                     private val identityComparator: Comparator<R>) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) = identityComparator.compare(oldList[oldItemPosition], newList[newItemPosition]) == 0

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) = oldList[oldItemPosition] == newList[newItemPosition]
}

fun <R> androidx.recyclerview.widget.RecyclerView.Adapter<*>.diffDSL(setup: DiffUtilsBuilder<R>.() -> Unit) {
    with(DiffUtilsBuilder<R>()) {
        adapter = this@diffDSL
        setup()
        build()
    }
}