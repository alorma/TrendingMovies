package com.alorma.myapplication.ui.movies

import androidx.test.espresso.intent.Intents
import com.alorma.domain.repository.ConfigurationRepository
import com.alorma.domain.repository.MoviesRepository
import com.alorma.myapplication.R
import com.alorma.myapplication.ui.base.*
import com.alorma.myapplication.ui.detail.MovieDetailActivity
import com.alorma.myapplication.ui.search.SearchActivity
import com.nhaarman.mockito_kotlin.mock
import com.schibsted.spain.barista.assertion.BaristaRecyclerViewAssertions.assertRecyclerViewItemCount
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn
import com.schibsted.spain.barista.interaction.BaristaListInteractions.clickListItem
import com.schibsted.spain.barista.interaction.BaristaListInteractions.scrollListToPosition
import com.schibsted.spain.barista.rule.BaristaRule
import org.junit.Rule
import org.junit.Test
import org.koin.dsl.context.ModuleDefinition

class MoviesActivityTest : BaseKoinTest() {

    @get:Rule
    val rule = BaristaRule.create(MoviesActivity::class.java)

    val moviesRepository: MoviesRepository = mock()
    val configRepository: ConfigurationRepository = mock()

    override fun ModuleDefinition.mockDependencies() {
        factory { moviesRepository }
        factory { configRepository }
    }

    override fun configureMocks() {
        configRepository.asValidConfig()
    }

    @Test
    fun onLoadError_showErrorOnScreen() {
        moviesRepository.asError()

        rule.launchActivity()

        assertDisplayed(R.string.generic_error)
    }

    @Test
    fun onLoadItems_showOnScreen() {
        moviesRepository.asListValidData(50)

        rule.launchActivity()

        assertRecyclerViewItemCount(R.id.recycler, 50)
        assertDisplayed("Title 1")
        assertDisplayed("Title 2")
    }

    @Test
    fun onClickItem_openDetail() {
        Intents.init()
        intending<MovieDetailActivity>()

        moviesRepository.asListValidData(50)

        rule.launchActivity()

        clickListItem(R.id.recycler, 1)

        intended<MovieDetailActivity>()
        Intents.release()
    }

    @Test
    fun onClickSearch_openSearch() {
        Intents.init()
        intending<SearchActivity>()

        moviesRepository.asEmptyList()

        rule.launchActivity()

        clickOn(R.id.fabSearch)

        intended<SearchActivity>()
        Intents.release()
    }

    @Test
    fun onLoadManyItems_onScroll_loadMore() {
        moviesRepository.asListValidData(50)
        moviesRepository.asListNextPageValidData(75)

        rule.launchActivity()

        assertRecyclerViewItemCount(R.id.recycler, 50)

        scrollListToPosition(R.id.recycler, 49)

        assertRecyclerViewItemCount(R.id.recycler, 75)
    }
}