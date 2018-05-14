package com.alorma.myapplication.ui.movies

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import com.alorma.myapplication.R
import com.alorma.myapplication.TrendingMoviesApp.Companion.component
import com.alorma.myapplication.ui.common.BaseView
import com.alorma.myapplication.ui.common.DslAdapter
import com.alorma.myapplication.ui.common.adapterDsl
import com.alorma.myapplication.ui.movies.di.MoviesModule
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.android.synthetic.main.row_tv_movie_list.view.*
import javax.inject.Inject

class MoviesActivity : AppCompatActivity(), BaseView<MoviesStates.MovieState> {

    companion object {
        const val OFFSET_LAZY_LOAD = 4
    }

    @Inject
    lateinit var presenter: MoviesPresenter

    @Inject
    lateinit var actions: MoviesActions

    private lateinit var adapter: DslAdapter<MoviewItemVM>

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

        component add MoviesModule(this) inject this

        initView()

        presenter init this
        presenter reduce actions.load()
    }

    private fun initView() {
        val columns = resources.getInteger(R.integer.columns_movies)
        recycler.layoutManager = GridLayoutManager(this@MoviesActivity, columns)
        adapter = adapterDsl(recycler) {
            item {
                layout = R.layout.row_tv_movie_list
                bindView { view, movie ->
                    view.text.text = movie.title
                    view.votes.text = movie.votes
                    loadMovieImage(view.image, movie)
                }
                onClick {
                    presenter reduce actions.detail(it)
                }
            }

            diff { it.id }
        }

        fabSearch.setOnClickListener {
            presenter reduce actions.search()
        }
    }

    private fun loadMovieImage(image: ImageView, moviewItem: MoviewItemVM) {
        moviewItem.image?.let {
            val requestOptions = RequestOptions().apply {
                placeholder(R.color.grey_300)
                error(R.color.grey_300)
            }

            image.contentDescription = moviewItem.title

            val requestManager = Glide.with(image).setDefaultRequestOptions(requestOptions)
            requestManager
                    .load(it)
                    .into(image)
        } ?: image.setImageResource(R.color.grey_300)
    }

    override fun render(state: MoviesStates.MovieState) {
        when (state) {
            is MoviesStates.MovieState.Loading -> onLoading(state)
            is MoviesStates.MovieState.Success -> onSuccess(state)
            is MoviesStates.MovieState.Error -> onError(state)
        }
    }

    private fun onLoading(state: MoviesStates.MovieState.Loading) {
        centerText.visibility = View.GONE
        disablePagination()
    }

    private fun onSuccess(state: MoviesStates.MovieState.Success) {
        centerText.visibility = View.GONE
        enablePagination()
        adapter.update(state.items)
    }

    private fun onError(state: MoviesStates.MovieState.Error) {
        with(centerText) {
            visibility = View.VISIBLE
            text = state.text
        }
    }

    private fun enablePagination() = recycler.addOnScrollListener(recyclerViewListener)

    private fun disablePagination() = recycler.removeOnScrollListener(recyclerViewListener)
}