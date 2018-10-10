package com.alorma.myapplication.ui.movies

import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
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
import org.hamcrest.Matcher
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
        intending(getMatcherOpenDetailActivity()).respondWith(getGenericResult())

        moviesRepository.asListValidData(50)

        rule.launchActivity()

        clickListItem(R.id.recycler, 1)

        intended(getMatcherOpenDetailActivity())
        Intents.release()
    }

    @Test
    fun onClickSearch_openSearch() {
        Intents.init()
        intending(getMatcherOpenSearchActivity()).respondWith(getGenericResult())

        moviesRepository.asListValidData(0)

        rule.launchActivity()

        clickOn(R.id.fabSearch)

        intended(getMatcherOpenSearchActivity())
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

    private fun getMatcherOpenDetailActivity(): Matcher<Intent> = hasComponent(MovieDetailActivity::class.java.name)

    private fun getMatcherOpenSearchActivity(): Matcher<Intent> = hasComponent(SearchActivity::class.java.name)

    private fun getGenericResult(): Instrumentation.ActivityResult = Instrumentation.ActivityResult(2, Intent())
}