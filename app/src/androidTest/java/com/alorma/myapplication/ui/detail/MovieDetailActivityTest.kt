package com.alorma.myapplication.ui.detail

import androidx.test.espresso.intent.Intents
import com.alorma.domain.repository.ConfigurationRepository
import com.alorma.domain.repository.MoviesRepository
import com.alorma.myapplication.R
import com.alorma.myapplication.config.app
import com.alorma.myapplication.ui.base.*
import com.nhaarman.mockito_kotlin.mock
import com.schibsted.spain.barista.assertion.BaristaRecyclerViewAssertions.assertRecyclerViewItemCount
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertNotDisplayed
import com.schibsted.spain.barista.interaction.BaristaListInteractions.clickListItem
import com.schibsted.spain.barista.rule.BaristaRule
import org.junit.Rule
import org.junit.Test
import org.koin.dsl.context.ModuleDefinition

class MovieDetailActivityTest : BaseKoinTest() {

    companion object {
        const val OVERVIEW: String = "This is an overview"
        const val VOTE: Float = 5.4f
        const val ID: Int = 12
    }

    @get:Rule
    val rule = BaristaRule.create(MovieDetailActivity::class.java)

    val moviesRepository: MoviesRepository = mock()
    val configRepository: ConfigurationRepository = mock()

    override fun ModuleDefinition.mockDependencies() {
        factory { moviesRepository }
        factory { configRepository }
    }

    override fun configureMocks() {
        configRepository.asValidConfig()
        moviesRepository.asSimilarEmptyList(ID)
    }

    @Test
    fun onLoadMovie_renderTitleOnScreen() {
        moviesRepository.asMovieValidData(ID)

        launchWithId()

        assertDisplayed("Title $ID")
    }

    @Test
    fun onLoadMovie_renderOverviewOnScreen() {
        moviesRepository.asMovieValidData(ID)

        launchWithId()

        assertDisplayed(OVERVIEW)
    }

    @Test
    fun onLoadMovie_renderVotesOnScreen() {
        moviesRepository.asMovieValidData(ID)

        launchWithId()

        assertDisplayed(String.format("%.1f", VOTE))
    }

    @Test
    fun onLoadMovie_renderGenresOnScreen() {
        moviesRepository.asMovieValidData(ID)

        launchWithId()

        assertDisplayed("Comedy")
        assertDisplayed("Drama")
    }

    @Test
    fun onLoadWithoutSimilarMovies_notDisplaySimilarMoviesLabel() {
        moviesRepository.asMovieValidData(ID)

        launchWithId()

        assertNotDisplayed(R.id.similarMoviesLabel)
    }

    @Test
    fun onLoadWithSimilarMovies_displaySimilarMoviesLabel() {
        moviesRepository.asMovieValidData(ID)
        moviesRepository.asSimilarListValidData(ID, 30)

        launchWithId()

        assertDisplayed(R.id.similarMoviesLabel)
    }

    @Test
    fun onLoadWithSimilarMovies_displaySimilarMovies() {
        moviesRepository.asMovieValidData(ID)
        moviesRepository.asSimilarListValidData(ID, 30)

        launchWithId()

        assertRecyclerViewItemCount(R.id.similarMoviesRecycler, 30)
    }

    @Test
    fun onClickSimilarMovie_displayOtherDetail() {
        Intents.init()
        intending<MovieDetailActivity>()

        moviesRepository.asMovieValidData(ID)
        moviesRepository.asSimilarListValidData(ID, 30)

        launchWithId()

        clickListItem(R.id.similarMoviesRecycler, 1)

        intended<MovieDetailActivity>(2)

        Intents.release()
    }

    private fun launchWithId(id: Int = ID) {
        rule.launchActivity(MovieDetailActivity.launch(app, id, "Title $id"))
    }
}