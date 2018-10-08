package com.alorma.myapplication.ui.common

import androidx.appcompat.widget.Toolbar
import android.view.View
import com.alorma.myapplication.R

@DslMarker
annotation class ToolbarDsl

@ToolbarDsl
class ItemBuilder {

    lateinit var action: () -> Unit
    var id: Int = 0

    fun build() = id to action
}

@ToolbarDsl
class ViewItemBuilder {

    var id: Int = 0
    lateinit var setupAction: (View) -> Unit

    @Suppress("UNCHECKED_CAST")
    inline fun <reified V : View> setup(noinline setup: (V) -> Unit) {
        this.setupAction = setup as (View) -> Unit
    }
}

@ToolbarDsl
class BackBuilder {
    var action: (() -> Unit)? = null
    var icon: Int = R.drawable.ic_arrow

    fun setup(toolbar: Toolbar) {
        action?.let { backAction ->
            toolbar.setNavigationIcon(icon)
            toolbar.setNavigationOnClickListener {
                backAction()
            }
        }
    }
}

@ToolbarDsl
class ToolbarBuilder {

    lateinit var toolbar: Toolbar
    var back: BackBuilder? = null
    var title: Any = Any()
    var menu: Int = 0
    private var items = mutableListOf<Pair<Int, () -> Unit>>()
    private var viewItems = mutableListOf<Pair<Int, ViewItemBuilder>>()

    fun build(): Toolbar {
        when (title) {
            is Int -> {
                val stringId = title as Int
                if (stringId != 0) {
                    toolbar.title = toolbar.resources.getString(stringId)
                }
            }
            is String -> toolbar.title = title as String
        }

        if (menu != 0) {
            toolbar.inflateMenu(menu)
        }

        back?.setup(toolbar)

        toolbar.setOnMenuItemClickListener {
            items.firstOrNull { item -> it.itemId == item.first }
                    ?.second
                    ?.invoke()
            true
        }

        viewItems.forEach { pair ->
            toolbar.menu?.getItem(pair.first)?.actionView?.let {
                pair.second.setupAction(it)
            }
        }

        return toolbar
    }

    fun item(id: Int = 0, setup: ItemBuilder.() -> Unit) {
        val builder = ItemBuilder()
        builder.id = id
        builder.setup()
        items.add(builder.build())
    }

    fun viewItem(id: Int = 0, setup: ViewItemBuilder.() -> Unit) {
        val builder = ViewItemBuilder()
        builder.id = id
        builder.setup()
        viewItems.add(id to builder)
    }

    fun back(setup: BackBuilder.() -> Unit) {
        back = BackBuilder().apply(setup)
    }
}

@ToolbarDsl
fun toolbarDSL(setup: ToolbarBuilder.() -> Unit) {
    with(ToolbarBuilder()) {
        setup()
        build()
    }
}

fun Toolbar.dsl(setup: ToolbarBuilder.() -> Unit) {
    with(ToolbarBuilder()) {
        toolbar = this@dsl
        setup()
        build()
    }
}