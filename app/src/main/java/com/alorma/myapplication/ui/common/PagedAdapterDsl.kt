package com.alorma.myapplication.ui.common

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

@DslMarker
annotation class PagedAdapterDsl

@AdapterDsl
class PagedHolderBuilder<M> {

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

    fun build(parent: ViewGroup): PagedDslAdapter.ViewHolder<M> {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        return PagedDslAdapter.ViewHolder(inflater.inflate(layout, parent, false), this)
    }
}

@AdapterDsl
class PagedAdapterDslBuilder<M> {

    private lateinit var holderBuilder: PagedHolderBuilder<M>
    private lateinit var comparator: Comparator<M>

    fun build(): PagedDslAdapter<M> = PagedDslAdapter(holderBuilder, comparator)

    fun item(setup: PagedHolderBuilder<M>.() -> Unit) {
        this.holderBuilder = PagedHolderBuilder<M>().apply(setup)
    }

    fun diff(comparator: (o1: M) -> Int) {
        this.comparator = compareBy { comparator(it) }
    }
}

class PagedDslAdapter<M>(@LayoutRes private val holderBuilder: PagedHolderBuilder<M>,
                         private val comparator: Comparator<M>)
    : RecyclerView.Adapter<PagedDslAdapter.ViewHolder<M>>() {

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

    fun clear() {

    }

    class ViewHolder<in M>(itemView: View,
                           private val holderBuilder: PagedHolderBuilder<M>)
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
fun <M> pagedAdapterDsl(recyclerView: RecyclerView,
                        setup: PagedAdapterDslBuilder<M>.() -> Unit): PagedDslAdapter<M> =
        with(PagedAdapterDslBuilder<M>()) {
            setup()
            build().also { recyclerView.adapter = it }
        }