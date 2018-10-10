package com.alorma.myapplication.ui.movies

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alorma.myapplication.R
import com.alorma.myapplication.ui.common.DslAdapter
import com.alorma.myapplication.ui.common.adapterDsl
import com.alorma.myapplication.ui.common.createPagination
import com.alorma.myapplication.ui.detail.MovieDetailActivity
import com.alorma.myapplication.ui.search.SearchActivity
import com.alorma.presentation.movies.*
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.android.synthetic.main.row_tv_movie_list.view.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MoviesActivity : AppCompatActivity() {

    val glide: RequestManager by inject()
    val actions: MoviesActions by inject()
    val moviesViewModel: MoviesViewModel by viewModel()

    private lateinit var adapter: DslAdapter<MovieItemVM>

    private val recyclerViewListener: RecyclerView.OnScrollListener by lazy {
        recycler.createPagination {
            moviesViewModel reduce actions.loadPage()
            disablePagination()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        moviesViewModel.observe(this) {
            onState { render(it) }
            onRoute { navigate(it) }
        }

        initView()

        moviesViewModel reduce actions.load()
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
                    moviesViewModel reduce actions.detail(it)
                }
            }

            diff { it.id }
        }

        fabSearch.setOnClickListener {
            moviesViewModel reduce actions.search()
        }
    }

    private fun loadMovieImage(image: ImageView, movieItem: MovieItemVM) {
        movieItem.image?.let {
            val requestOptions = RequestOptions().apply {
                placeholder(R.color.grey_300)
                error(R.color.grey_300)
            }

            image.contentDescription = movieItem.title

            val requestManager = glide.setDefaultRequestOptions(requestOptions)
            requestManager
                    .load(it)
                    .into(image)
        } ?: image.setImageResource(R.color.grey_300)
    }

    private fun render(state: MoviesStates.MovieState) {
        when (state) {
            is MoviesStates.MovieState.Loading -> onLoading()
            is MoviesStates.MovieState.Success -> onSuccess(state)
            is MoviesStates.MovieState.Error -> onError(state)
        }
    }

    private fun navigate(it: MoviesRoutes.MovieRoute) {
        when (it) {
            is MoviesRoutes.MovieRoute.DetailRoute -> openDetail(it)
            MoviesRoutes.MovieRoute.Search -> openSearch()
        }
    }

    private fun openDetail(it: MoviesRoutes.MovieRoute.DetailRoute) {
        startActivity(MovieDetailActivity.launch(this, it.id, it.title))
    }

    private fun openSearch() {
        startActivity(SearchActivity.launch(this))
    }

    private fun onLoading() {
        centerText.visibility = View.INVISIBLE
        if (recycler.adapter?.itemCount == 0) {
            loaderProgress.visibility = View.VISIBLE
        }
        disableRetry()
    }

    private fun onSuccess(state: MoviesStates.MovieState.Success) {
        centerText.visibility = View.INVISIBLE
        loaderProgress.visibility = View.INVISIBLE
        disableRetry()
        enablePagination()
        adapter.update(state.items)
    }

    private fun disableRetry() {
        with(retryButton) {
            visibility = View.INVISIBLE
            isEnabled = false
            setOnClickListener { }
        }
    }

    private fun onError(state: MoviesStates.MovieState.Error) {
        with(centerText) {
            visibility = View.VISIBLE
            text = state.text
        }
        with(retryButton) {
            visibility = View.VISIBLE
            isEnabled = true
            setOnClickListener {
                moviesViewModel reduce actions.load()
            }
        }
        loaderProgress.visibility = View.INVISIBLE
    }

    private fun enablePagination() = recycler.addOnScrollListener(recyclerViewListener)

    private fun disablePagination() = recycler.removeOnScrollListener(recyclerViewListener)
}