package com.alorma.myapplication.ui.movies

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import com.alorma.myapplication.R
import com.alorma.myapplication.TrendingMoviesApp.Companion.component
import com.alorma.myapplication.ui.common.DslAdapter
import com.alorma.myapplication.ui.common.adapterDsl
import com.alorma.myapplication.ui.common.pagination
import com.alorma.myapplication.ui.movies.di.MoviesModule
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.android.synthetic.main.row_tv_movie_list.view.*
import javax.inject.Inject

class MoviesActivity : AppCompatActivity() {

    companion object {
        const val OFFSET_LAZY_LOAD = 4
    }

    @Inject
    lateinit var actions: MoviesActions

    private lateinit var adapter: DslAdapter<MovieItemVM>

    private val recyclerViewListener: RecyclerView.OnScrollListener by lazy {
        recycler.pagination {
            viewModel reduce actions.loadPage()
            disablePagination()
        }
    }

    @Inject
    lateinit var viewModel: MoviesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        component add MoviesModule(this) inject this

        viewModel.init(this,
                Observer {
                    it?.let { render(it) }
                }
        )

        initView()

        viewModel reduce actions.load()
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
                    viewModel reduce actions.detail(it)
                }
            }

            diff { it.id }
        }

        fabSearch.setOnClickListener {
            viewModel reduce actions.search()
        }
    }

    private fun loadMovieImage(image: ImageView, movieItem: MovieItemVM) {
        movieItem.image?.let {
            val requestOptions = RequestOptions().apply {
                placeholder(R.color.grey_300)
                error(R.color.grey_300)
            }

            image.contentDescription = movieItem.title

            val requestManager = Glide.with(image).setDefaultRequestOptions(requestOptions)
            requestManager
                    .load(it)
                    .into(image)
        } ?: image.setImageResource(R.color.grey_300)
    }

    fun render(state: MoviesStates.MovieState) {
        when (state) {
            is MoviesStates.MovieState.Loading -> onLoading(state)
            is MoviesStates.MovieState.Success -> onSuccess(state)
            is MoviesStates.MovieState.Error -> onError(state)
        }
    }

    private fun onLoading(state: MoviesStates.MovieState.Loading) {
        centerText.visibility = View.GONE
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