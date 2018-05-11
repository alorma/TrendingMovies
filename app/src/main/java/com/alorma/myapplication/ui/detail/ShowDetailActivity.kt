package com.alorma.myapplication.ui.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.alorma.myapplication.R
import com.alorma.myapplication.ui.common.dsl
import kotlinx.android.synthetic.main.detail_activity.*
import com.alorma.myapplication.TrendingTvApp.Companion.component
import com.alorma.myapplication.ui.common.BaseView
import com.alorma.myapplication.ui.detail.di.DetailModule
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
            back {
                action = {
                    finish()
                }
            }
        }

        intent.extras?.let {
            it.getString(EXTRA_TITLE)?.let {
                textTitle.text = it
            }
        }
    }

    private fun initData() {
        intent?.extras?.getInt(EXTRA_ID, -1)?.takeIf { it != -1 }?.let {
            presenter reduce actions.load(it)
        }
    }

    override fun render(state: DetailStates.DetailState) {

    }

}