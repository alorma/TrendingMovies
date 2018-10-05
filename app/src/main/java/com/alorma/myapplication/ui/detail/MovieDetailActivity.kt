package com.alorma.myapplication.ui.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.alorma.myapplication.R
import com.alorma.myapplication.ui.common.DslAdapter
import com.alorma.myapplication.ui.common.adapterDsl
import com.alorma.myapplication.ui.common.createPagination
import com.alorma.myapplication.ui.common.dsl
import com.alorma.myapplication.ui.movies.MovieItemVM
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.detail_activity.*
import kotlinx.android.synthetic.main.detail_content.*
import kotlinx.android.synthetic.main.detail_genre_chip.view.*
import kotlinx.android.synthetic.main.row_similar_movie.view.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MovieDetailActivity : AppCompatActivity() {
    companion object {
        private const val EXTRA_ID = "extra_id"
        private const val EXTRA_TITLE = "extra_title"

        fun launch(context: Context, id: Int, title: String): Intent =
                Intent(context, MovieDetailActivity::class.java).apply {
                    putExtra(EXTRA_ID, id)
                    putExtra(EXTRA_TITLE, title)
                }
    }

    val actions: DetailActions by inject()
    val movieDetailViewModel: MovieDetailViewModel by viewModel()

    private lateinit var similarMoviesAdapter: DslAdapter<MovieItemVM>
    private lateinit var genresAdapter: DslAdapter<String>

    private val recyclerViewListener: RecyclerView.OnScrollListener by lazy {
        similarMoviesRecycler.createPagination {
            movieDetailViewModel reduce actions.loadSimilarPage()
            disablePagination()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_activity)

        movieDetailViewModel.observe(this) {
            onState { render(it) }
            onRoute { navigate(it) }
        }

        initData()

        toolbar.dsl {
            back { action = { movieDetailViewModel reduce actions.back() } }
        }
    }

    private fun initData() {
        initGenres()
        initSimilarMovies()

        intent.extras?.let {
            it.getString(EXTRA_TITLE)?.let { title ->
                toolbar.title = title
            }
            it.getInt(EXTRA_ID, -1).takeIf { id -> id != -1 }?.let { id ->
                movieDetailViewModel reduce actions.load(id)
            } ?: movieDetailViewModel reduce actions.back()
        }
    }

    private fun initSimilarMovies() {
        genresAdapter = adapterDsl(genresRecycler) {
            item {
                layout = R.layout.detail_genre_chip
                bindView { view, genre ->
                    view.genreText.text = genre
                }
            }
            diff { it.hashCode() }
        }
        similarMoviesRecycler.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this@MovieDetailActivity,
                androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL, false)
    }

    private fun initGenres() {
        similarMoviesAdapter = adapterDsl(similarMoviesRecycler) {
            item {
                layout = R.layout.row_similar_movie
                bindView { view, movieItemVM ->
                    loadSimilarMovieImage(view.image, movieItemVM)
                    view.text.text = movieItemVM.title
                }
                onClick {
                    movieDetailViewModel reduce actions.openSimilarMovie(it)
                }
            }
            diff { it.id }
        }
        genresRecycler.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this@MovieDetailActivity,
                androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL, false)
    }

    private fun render(state: DetailStates.DetailState) {
        when (state) {
            is DetailStates.DetailState.Success -> onSuccess(state)
            is DetailStates.DetailState.SimilarMovies -> onSimilarMovies(state.movies)
        }
    }

    private fun navigate(it: DetailRoutes.DetailRoute) {
        when (it) {
            is DetailRoutes.DetailRoute.Detail -> openDetail(it)
            DetailRoutes.DetailRoute.Back -> finish()
        }
    }

    private fun openDetail(it: DetailRoutes.DetailRoute.Detail) {
        startActivity(MovieDetailActivity.launch(this, it.id, it.title))
    }

    private fun onSuccess(state: DetailStates.DetailState.Success) {
        setTexts(state.detail)
        showImage(state.detail)
        showGenres(state.detail)
        onSimilarMovies(state.similarMovies)
    }

    private fun setTexts(movieDetailVM: MovieDetailVM) {
        toolbar.title = movieDetailVM.title
        toolbar.subtitle = movieDetailVM.date
        textDescription.text = movieDetailVM.overView
        textVotes.text = movieDetailVM.vote
    }

    private fun showImage(movieDetailVM: MovieDetailVM) {
        movieDetailVM.image?.let {
            val requestOptions = RequestOptions().apply {
                placeholder(R.color.grey_300)
                error(R.color.grey_300)
            }

            val requestManager = Glide.with(heroImage).setDefaultRequestOptions(requestOptions)
            requestManager
                    .load(it)
                    .into(heroImage)
        } ?: heroImage.setImageResource(R.color.grey_300)
    }

    private fun showGenres(movieDetailVM: MovieDetailVM) {
        genresAdapter.update(movieDetailVM.genres)
    }

    private fun onSimilarMovies(items: List<MovieItemVM>) {
        if (items.isEmpty()) {
            similarMoviesLabel.visibility = View.INVISIBLE
            return
        }
        similarMoviesLabel.visibility = View.VISIBLE
        similarMoviesAdapter.update(items)
        enablePagination()
    }

    private fun loadSimilarMovieImage(image: ImageView, movieItem: MovieItemVM) {
        movieItem.image?.let {
            val requestOptions = RequestOptions().apply {
                placeholder(R.color.grey_300)
                error(R.color.grey_300)
            }

            val requestManager = Glide.with(image).setDefaultRequestOptions(requestOptions)
            requestManager
                    .load(it)
                    .into(image)
        } ?: image.setImageResource(R.color.grey_300)
    }

    private fun enablePagination() = similarMoviesRecycler.addOnScrollListener(recyclerViewListener)

    private fun disablePagination() = similarMoviesRecycler.removeOnScrollListener(recyclerViewListener)
}
