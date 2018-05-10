package com.alorma.myapplication.ui.shows

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.alorma.myapplication.R
import com.alorma.myapplication.TrendingTvApp.Companion.component
import com.alorma.myapplication.ui.common.BaseView
import com.alorma.myapplication.ui.common.DslAdapter
import com.alorma.myapplication.ui.common.adapterDsl
import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.android.synthetic.main.row_tv_show_list.view.*
import javax.inject.Inject

class ShowsActivity : AppCompatActivity(), BaseView<ShowsRoute, ShowsState> {

    @Inject
    lateinit var presenter: ShowsPresenter

    @Inject
    lateinit var actions: ShowsAction

    private lateinit var adapter: DslAdapter<TvShowVM>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        component inject this

        initView()

        presenter init this
        presenter reduce actions.load()
    }

    private fun initView() {
        recycler.layoutManager = LinearLayoutManager(this@ShowsActivity)
        adapter = adapterDsl(recycler) {
            item {
                layout = R.layout.row_tv_show_list
                bindView { view, tvShow ->
                    view.title.text = tvShow.title
                }
                onClick {
                    presenter reduce actions.detail(it)
                }
            }

            diff { it.id }
        }
    }

    override fun render(state: ShowsState) {
        when (state) {
            is ShowsState.Loading -> onLoading(state)
            is ShowsState.Success -> onSuccess(state)
            is ShowsState.Error -> onError(state)
        }
    }

    private fun onLoading(state: ShowsState.Loading) {
        centerText.visibility = View.GONE
    }

    private fun onSuccess(state: ShowsState.Success) {
        centerText.visibility = View.GONE
        adapter.update(state.items)
    }

    private fun onError(state: ShowsState.Error) {
        with(centerText) {
            visibility = View.VISIBLE
            text = state.text
        }
    }

    override fun navigate(route: ShowsRoute) {

    }
}