package com.alorma.myapplication.ui.movies

import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import com.alorma.myapplication.R
import com.alorma.myapplication.config.configureRxThreading
import com.alorma.myapplication.domain.model.Images
import com.alorma.myapplication.domain.model.Movie
import com.alorma.myapplication.data.repository.ConfigurationRepository
import com.alorma.myapplication.data.repository.MoviesRepository
import com.alorma.myapplication.ui.detail.MovieDetailActivity
import com.alorma.myapplication.ui.search.SearchActivity
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.mock
import com.schibsted.spain.barista.assertion.BaristaRecyclerViewAssertions.assertRecyclerViewItemCount
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn
import com.schibsted.spain.barista.interaction.BaristaListInteractions.clickListItem
import com.schibsted.spain.barista.interaction.BaristaListInteractions.scrollListToPosition
import com.schibsted.spain.barista.rule.BaristaRule
import io.reactivex.Single
import org.hamcrest.Matcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext.loadKoinModules
import java.util.*

class MoviesActivityTest {

    @get:Rule
    val rule = BaristaRule.create(MoviesActivity::class.java)

    val moviesRepository: MoviesRepository = mock()
    val configRepository: ConfigurationRepository = mock()

    val mockModule = module {
        factory { moviesRepository }
        factory { configRepository }
    }

    init {
        configureRxThreading()
    }

    @Before
    fun setup() {
        loadKoinModules(mockModule)
        given(configRepository.getConfig()).willReturn(Single.just(mock()))
    }

    @Test
    fun onLoadError_showErrorOnScreen() {
        given(moviesRepository.listAll()).willReturn(Single.error(Exception()))

        rule.launchActivity()

        assertDisplayed(R.string.generic_error)
    }

    @Test
    fun onLoadItems_showOnScreen() {
        val items = generateItems(50)
        given(moviesRepository.listAll()).willReturn(Single.just(items))

        rule.launchActivity()

        assertRecyclerViewItemCount(R.id.recycler, 50)
        assertDisplayed("Title 1")
        assertDisplayed("Title 2")
    }

    @Test
    fun onClickItem_openDetail() {
        Intents.init()
        intending(getMatcherOpenDetailActivity()).respondWith(getGenericResult())

        val items = generateItems(50)
        given(moviesRepository.listAll()).willReturn(Single.just(items))

        rule.launchActivity()

        clickListItem(R.id.recycler, 1)

        intended(getMatcherOpenDetailActivity())
        Intents.release()
    }

    @Test
    fun onClickSearch_openSearch() {
        Intents.init()
        intending(getMatcherOpenSearchActivity()).respondWith(getGenericResult())

        val items = generateItems(50)
        given(moviesRepository.listAll()).willReturn(Single.just(items))

        rule.launchActivity()

        clickOn(R.id.fabSearch)

        intended(getMatcherOpenSearchActivity())
        Intents.release()
    }

    @Test
    fun onLoadManyItems_onScroll_loadMore() {
        val items = generateItems(50)
        given(moviesRepository.listAll()).willReturn(Single.just(items))
        val itemsPage = generateItems(75)
        given(moviesRepository.listNextPage()).willReturn(Single.just(itemsPage))

        rule.launchActivity()

        assertRecyclerViewItemCount(R.id.recycler, 50)

        scrollListToPosition(R.id.recycler, 49)

        assertRecyclerViewItemCount(R.id.recycler, 75)
    }

    private fun generateItems(number: Int): List<Movie> = (1..number).map { generateItem(it) }

    private fun generateItem(id: Int): Movie = Movie(id, "Title $id", "", Images("", ""), Date(), 0f, listOf())

    private fun getMatcherOpenDetailActivity(): Matcher<Intent> = hasComponent(MovieDetailActivity::class.java.name)

    private fun getMatcherOpenSearchActivity(): Matcher<Intent> = hasComponent(SearchActivity::class.java.name)

    private fun getGenericResult(): Instrumentation.ActivityResult = Instrumentation.ActivityResult(2, Intent())
}