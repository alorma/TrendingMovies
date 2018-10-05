package com.alorma.myapplication.ui.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.alorma.myapplication.R
import com.alorma.myapplication.ui.common.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.row_search.view.*
import kotlinx.android.synthetic.main.search_activity.*
import javax.inject.Inject

class SearchActivity : AppCompatActivity() {
    companion object {
        fun launch(context: Context): Intent = Intent(context, SearchActivity::class.java)
    }

    @Inject
    lateinit var actions: SearchActions

    @Inject
    lateinit var viewModel: SearchViewModel

    private lateinit var adapter: DslAdapter<MovieSearchItemVM>

    private val recyclerViewListener: RecyclerView.OnScrollListener by lazy {
        recycler.createPagination {
            viewModel reduce actions.page()
            disablePagination()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_activity)

        viewModel.observe(this) {
            onState { render(it) }
        }

        initView()
    }

    private fun initView() {
        initToolbar()
        initRecycler()
        centerText.setText(R.string.search_central_hint)
    }

    private fun initToolbar() {
        toolbar.dsl {
            menu = R.menu.search_menu
            back {
                action = { viewModel reduce actions.back() }
            }
        }
        toolbar.searchDsl {
            id = R.id.action_search
            open = true
            textSubmitted {
                viewModel reduce actions.query(it)
                true
            }
            textChange {
                viewModel reduce actions.query(it)
                true
            }
            onClose {
                viewModel reduce actions.back()
                true
            }
        }
    }

    private fun initRecycler() {
        adapter = adapterDsl(recycler) {
            item {
                layout = R.layout.row_search
                bindView { view, movie ->
                    view.title.text = movie.title
                    view.overview.text = movie.overview
                    view.votes.text = movie.votes
                    view.year.text = movie.year
                    loadMovieImage(view.image, movie)
                }
                onClick { viewModel reduce actions.detail(it) }
            }
            diff { it.id }
        }
        recycler.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
    }

    private fun loadMovieImage(image: ImageView, movieItem: MovieSearchItemVM) {
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

    private fun render(state: SearchStates.SearchState) {
        when (state) {
            is SearchStates.SearchState.Loading -> onLoading()
            is SearchStates.SearchState.SearchResult -> onResult(state)
            is SearchStates.SearchState.Error -> onError(state)
        }
    }

    private fun onLoading() {
        centerText.visibility = View.INVISIBLE
        if (recycler.adapter?.itemCount == 0) {
            loaderProgress.visibility = View.VISIBLE
        }
        disableRetry()
    }

    private fun onResult(state: SearchStates.SearchState.SearchResult) {
        loaderProgress.visibility = View.INVISIBLE
        centerText.visibility = View.INVISIBLE
        adapter.update(state.items)
        enablePagination()
        disableRetry()
    }

    private fun disableRetry() {
        with(retryButton) {
            visibility = View.INVISIBLE
            isEnabled = false
            setOnClickListener { }
        }
    }

    private fun onError(it: SearchStates.SearchState.Error) {
        with(centerText) {
            visibility = android.view.View.VISIBLE
            text = it.text
        }
        with(retryButton) {
            visibility = android.view.View.VISIBLE
            isEnabled = true
            setOnClickListener {
                viewModel reduce actions.retry()
            }
        }
        loaderProgress.visibility = View.INVISIBLE
    }

    private fun enablePagination() = recycler.addOnScrollListener(recyclerViewListener)

    private fun disablePagination() = recycler.removeOnScrollListener(recyclerViewListener)
}