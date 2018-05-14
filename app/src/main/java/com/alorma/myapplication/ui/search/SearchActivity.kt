package com.alorma.myapplication.ui.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.alorma.myapplication.R
import com.alorma.myapplication.TrendingMoviesApp.Companion.component
import com.alorma.myapplication.ui.common.BaseView
import com.alorma.myapplication.ui.common.DslAdapter
import com.alorma.myapplication.ui.common.adapterDsl
import com.alorma.myapplication.ui.movies.MoviesActivity
import com.alorma.myapplication.ui.search.di.SearchModule
import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.android.synthetic.main.row_search.view.*
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
        object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                (recyclerView.layoutManager as? LinearLayoutManager)?.apply {
                    val firstVisibleItemPosition = findFirstVisibleItemPosition()
                    val last = childCount + firstVisibleItemPosition
                    if (last >= itemCount - MoviesActivity.OFFSET_LAZY_LOAD) {
                        presenter reduce actions.page()
                        disablePagination()
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_activity)

        component add SearchModule(this) inject this

        presenter init this

        initView()

        presenter reduce actions.query("ready")
    }

    private fun initView() {
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