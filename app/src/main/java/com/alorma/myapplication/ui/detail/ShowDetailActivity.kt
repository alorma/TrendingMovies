package com.alorma.myapplication.ui.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.alorma.myapplication.R
import com.alorma.myapplication.TrendingTvApp.Companion.component
import com.alorma.myapplication.ui.common.BaseView
import com.alorma.myapplication.ui.common.dsl
import com.alorma.myapplication.ui.detail.di.DetailModule
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.detail_activity.*
import javax.inject.Inject

class ShowDetailActivity : AppCompatActivity(), BaseView<DetailStates.DetailState> {
    companion object {
        private const val EXTRA_ID = "extra_id"
        private const val EXTRA_TITLE = "extra_title"

        fun launch(context: Context, id: Int, title: String): Intent =
                Intent(context, ShowDetailActivity::class.java).apply {
                    putExtra(EXTRA_ID, id)
                    putExtra(EXTRA_TITLE, title)
                }
    }

    @Inject
    lateinit var actions: DetailActions

    @Inject
    lateinit var presenter: ShowDetailPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_activity)

        component add DetailModule(this) inject this

        presenter init this

        initData()

        toolbar.dsl {
            back { action = { presenter reduce actions.back() } }
        }
    }

    private fun initData() {
        intent.extras?.let {
            it.getString(EXTRA_TITLE)?.let {
                toolbar.title = it
            }
            it.getInt(EXTRA_ID, -1).takeIf { it != -1 }?.let {
                presenter reduce actions.load(it)
            } ?: presenter reduce actions.back()
        }
    }

    override fun render(state: DetailStates.DetailState) {
        when (state) {
            is DetailStates.DetailState.Success -> onSuccess(state)
        }
    }

    private fun onSuccess(state: DetailStates.DetailState.Success) {
        with(state.detail) {
            toolbar.title = title
            textDescription.text = overView
            val requestOptions = RequestOptions().apply {
                placeholder(R.color.grey_300)
                error(R.color.grey_300)

            }
            textDate.text = date
            textVotes.text = vote
            val requestManager = Glide.with(heroImage)
                    .setDefaultRequestOptions(requestOptions)
            requestManager
                    .load(image)
                    .thumbnail(requestManager.load(thumb))
                    .into(heroImage)
        }

    }

}