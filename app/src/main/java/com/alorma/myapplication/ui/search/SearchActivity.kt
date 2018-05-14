package com.alorma.myapplication.ui.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.alorma.myapplication.TrendingMoviesApp.Companion.component
import com.alorma.myapplication.ui.common.BaseView
import com.alorma.myapplication.ui.search.di.SearchModule
import javax.inject.Inject

class SearchActivity : AppCompatActivity(), BaseView<SearchStates.SearchState> {
    companion object {
        fun launch(context: Context): Intent = Intent(context, SearchActivity::class.java)
    }

    @Inject
    lateinit var actions: SearchActions

    @Inject
    lateinit var presenter: SearchPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        component add SearchModule() inject this

        presenter init this
    }

    override fun render(state: SearchStates.SearchState) {

    }
}