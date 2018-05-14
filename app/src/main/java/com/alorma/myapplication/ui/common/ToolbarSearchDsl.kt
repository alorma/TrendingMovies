package com.alorma.myapplication.ui.common


import android.support.annotation.IdRes
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.view.MenuItem


@DslMarker
annotation class ToolbarSearchDsl

@ToolbarSearchDsl
class TextSearchBuilder {
    lateinit var action: (String) -> Boolean
}

@ToolbarSearchDsl
class ToolbarSearchBuilder {

    lateinit var toolbar: Toolbar
    @IdRes
    var id: Int = 0

    var open: Boolean = false

    private var closeAction: (() -> Boolean)? = null
    private var textChange: TextSearchBuilder? = null
    private var textSubmitted: TextSearchBuilder? = null

    fun build(): Toolbar {

        val menuItem = toolbar.menu?.findItem(id)
        (menuItem?.actionView as? SearchView)?.apply {
            setIconifiedByDefault(open.not())
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    return textSubmitted?.action?.invoke(query) ?: false
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    return textChange?.action?.invoke(newText) ?: false
                }
            })

            menuItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
                override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                    return true
                }

                override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                    return closeAction?.invoke() ?: false
                }

            })
        }

        return toolbar
    }

    fun textChange(setup: (String) -> Boolean) {
        textChange = TextSearchBuilder().apply {
            action = setup
        }
    }

    fun textSubmitted(setup: (String) -> Boolean) {
        textSubmitted = TextSearchBuilder().apply {
            action = setup
        }
    }

    fun onClose(close: () -> Boolean) {
        closeAction = close
    }
}

fun Toolbar.searchDsl(setup: ToolbarSearchBuilder.() -> Unit) {
    with(ToolbarSearchBuilder()) {
        toolbar = this@searchDsl
        setup()
        build()
    }
}