package com.alorma.myapplication.ui.common

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

@DslMarker
annotation class AdapterDsl

@AdapterDsl
class HolderBuilder<M> {

    @LayoutRes
    var layout: Int = 0
    lateinit var onBind: (View, M) -> Unit
    var onClick: ((M) -> Unit)? = null

    fun bindView(function: (View, M) -> Unit) {
        this.onBind = function
    }

    fun onClick(function: (M) -> Unit) {
        this.onClick = function
    }

    fun build(parent: ViewGroup): DslAdapter.ViewHolder<M> {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        return DslAdapter.ViewHolder(inflater.inflate(layout, parent, false), this)
    }
}

@AdapterDsl
class AdapterDslBuilder<M> {

    private lateinit var holderBuilder: HolderBuilder<M>
    private lateinit var comparator: Comparator<M>

    fun build(): DslAdapter<M> = DslAdapter(holderBuilder, comparator)

    fun item(setup: HolderBuilder<M>.() -> Unit) {
        this.holderBuilder = HolderBuilder<M>().apply(setup)
    }

    fun diff(comparator: (o1: M) -> Int) {
        this.comparator = compareBy { comparator(it) }
    }
}

class DslAdapter<M>(@LayoutRes private val holderBuilder: HolderBuilder<M>,
                    private val comparator: Comparator<M>)
    : RecyclerView.Adapter<DslAdapter.ViewHolder<M>>() {

    private val items: MutableList<M> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<M> =
            holderBuilder.build(parent)

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
                           private val holderBuilder: HolderBuilder<M>)
        : RecyclerView.ViewHolder(itemView) {

        fun bind(vm: M) {
            with(holderBuilder) {
                onBind(itemView, vm)
                itemView.setOnClickListener {
                    onClick?.invoke(vm)
                }
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