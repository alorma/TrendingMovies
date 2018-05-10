package com.alorma.myapplication.ui.common

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

@DslMarker
annotation class AdapterDsl

@AdapterDsl
class AdapterDslBuilder<M> {

    @LayoutRes
    var layout: Int = 0
    private lateinit var onBind: (View, M) -> Unit
    private lateinit var onClick: (M) -> Unit
    private lateinit var comparator: Comparator<M>

    fun build(): DslAdapter<M> = DslAdapter(layout, onBind, onClick, comparator)

    fun onBind(function: (View, M) -> Unit) {
        this.onBind = function
    }

    fun onClick(function: (M) -> Unit) {
        this.onClick = function
    }

    fun diff(comparator: (o1: M) -> Int) {
        this.comparator = compareBy { comparator(it) }
    }
}

class DslAdapter<M>(@LayoutRes val layout: Int,
                    private val onBind: (View, M) -> Unit,
                    private val onClick: (M) -> Unit,
                    private val comparator: Comparator<M>)
    : RecyclerView.Adapter<DslAdapter.ViewHolder<M>>() {

    private val items: MutableList<M> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<M> {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        return ViewHolder(inflater.inflate(layout, parent, false), onBind, onClick)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder<M>, position: Int) {
        holder.bind(items[position])
    }

    fun update(newItems: List<M>) {
        diffDSL<M> {
            oldList = items
            newList = newItems
            comparable = comparator
        }
    }

    class ViewHolder<in M>(itemView: View,
                           private val onBind: (View, M) -> Unit,
                           private val onClick: (M) -> Unit)
        : RecyclerView.ViewHolder(itemView) {

        fun bind(vm: M) {
            onBind(itemView, vm)
            itemView.setOnClickListener {
                onClick(vm)
            }
        }
    }
}


@AdapterDsl
fun <M> adapterDsl(recyclerView: RecyclerView,
                   setup: AdapterDslBuilder<M>.() -> Unit): DslAdapter<M> =
        with(AdapterDslBuilder<M>()) {
            setup()
            build().also { recyclerView.adapter = it }
        }