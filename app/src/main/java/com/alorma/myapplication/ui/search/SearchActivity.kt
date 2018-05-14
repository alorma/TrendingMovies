package com.alorma.myapplication.ui.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.alorma.myapplication.R
import com.alorma.myapplication.TrendingMoviesApp.Companion.component
import com.alorma.myapplication.ui.common.*
import com.alorma.myapplication.ui.search.di.SearchModule
import kotlinx.android.synthetic.main.row_search.view.*
import kotlinx.android.synthetic.main.search_activity.*
import javax.inject.Inject

class SearchActivity : AppCompatActivity(), BaseView<SearchStates.SearchState> {
    companion object {
        fun launch(context: Context): Intent = Intent(context, SearchActivity::class.java)
    }

    @Inject
    lateinit var actions: SearchActions

    @Inject
    lateinit var presenter: SearchPresenter

    private lateinit var adapter: DslAdapter<MovieSearchItemVM>

    private val recyclerViewListener: RecyclerView.OnScrollListener by lazy {
        recycler.pagination {
            presenter reduce actions.page()
            disablePagination()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_activity)

        component add SearchModule(this) inject this

        presenter init this

        initView()
    }

    private fun initView() {
        initToolbar()
        initRecycler()
    }

    private fun initToolbar() {
        toolbar.dsl {
            menu = R.menu.search_menu
        }
        toolbar.searchDsl {
            id = R.id.action_search
            open = true
            textSubmitted {
                presenter reduce actions.query(it)
                true
            }
            textChange {
                presenter reduce actions.query(it)
                true
            }
            onClose {
                presenter reduce actions.back()
                true
            }
        }
    }

    private fun initRecycler() {
        adapter = adapterDsl(recycler) {
            item {
                layout = R.layout.row_search
                bindView { view, movie ->
                    view.text.text = movie.title
                }
            }
            diff { it.id }
        }
        recycler.layoutManager = LinearLayoutManager(this)
    }

    override fun render(state: SearchStates.SearchState) {
        when (state) {
            is SearchStates.SearchState.SearchResult -> onResult(state)
        }
    }

    private fun onResult(state: SearchStates.SearchState.SearchResult) {
        adapter.update(state.items)
        enablePagination()
    }

    private fun enablePagination() = recycler.addOnScrollListener(recyclerViewListener)

    private fun disablePagination() = recycler.removeOnScrollListener(recyclerViewListener)
}