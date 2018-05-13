package com.alorma.myapplication.ui.shows

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import com.alorma.myapplication.R
import com.alorma.myapplication.TrendingTvApp.Companion.component
import com.alorma.myapplication.ui.common.BaseView
import com.alorma.myapplication.ui.common.DslAdapter
import com.alorma.myapplication.ui.common.adapterDsl
import com.alorma.myapplication.ui.shows.di.ShowsModule
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.android.synthetic.main.row_tv_show_list.view.*
import javax.inject.Inject

class ShowsActivity : AppCompatActivity(), BaseView<ShowsStates.ShowsState> {

    companion object {
        const val OFFSET_LAZY_LOAD = 4
    }

    @Inject
    lateinit var presenter: ShowsPresenter

    @Inject
    lateinit var actions: ShowsActions

    private lateinit var adapter: DslAdapter<TvShowVM>

    private val recyclerViewListener: RecyclerView.OnScrollListener by lazy {
        object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                (recyclerView.layoutManager as? LinearLayoutManager)?.apply {
                    val firstVisibleItemPosition = findFirstVisibleItemPosition()
                    val last = childCount + firstVisibleItemPosition
                    if (last >= itemCount - OFFSET_LAZY_LOAD) {
                        presenter reduce actions.loadPage()
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        component add ShowsModule(this) inject this

        initView()

        presenter init this
        presenter reduce actions.load()
    }

    private fun initView() {
        val columns = resources.getInteger(R.integer.columns_shows)
        recycler.layoutManager = GridLayoutManager(this@ShowsActivity, columns)
        adapter = adapterDsl(recycler) {
            item {
                layout = R.layout.row_tv_show_list
                bindView { view, tvShow ->
                    view.text.text = tvShow.title
                    view.votes.text = tvShow.votes
                    loadShowImage(view.image, tvShow)
                }
                onClick {
                    presenter reduce actions.detail(it)
                }
            }

            diff { it.id }
        }
    }

    private fun loadShowImage(image: ImageView, tvShow: TvShowVM) {
        tvShow.image?.let {
            val requestOptions = RequestOptions().apply {
                placeholder(R.color.grey_300)
                error(R.color.grey_300)
            }

            image.contentDescription = tvShow.title

            val requestManager = Glide.with(image).setDefaultRequestOptions(requestOptions)
            requestManager
                    .load(it)
                    .into(image)
        } ?: image.setImageResource(R.color.grey_300)
    }

    override fun render(state: ShowsStates.ShowsState) {
        when (state) {
            is ShowsStates.ShowsState.Loading -> onLoading(state)
            is ShowsStates.ShowsState.Success -> onSuccess(state)
            is ShowsStates.ShowsState.Error -> onError(state)
        }
    }

    private fun onLoading(state: ShowsStates.ShowsState.Loading) {
        centerText.visibility = View.GONE
        disablePagination()
    }

    private fun onSuccess(state: ShowsStates.ShowsState.Success) {
        centerText.visibility = View.GONE
        enablePagination()
        adapter.update(state.items)
    }

    private fun onError(state: ShowsStates.ShowsState.Error) {
        with(centerText) {
            visibility = View.VISIBLE
            text = state.text
        }
    }

    private fun enablePagination() = recycler.addOnScrollListener(recyclerViewListener)

    private fun disablePagination() = recycler.removeOnScrollListener(recyclerViewListener)
}