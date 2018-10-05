package com.alorma.myapplication.ui.detail

import android.content.Intent
import androidx.test.InstrumentationRegistry
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.VerificationModes.times
import androidx.test.espresso.intent.matcher.IntentMatchers
import com.alorma.myapplication.R
import com.alorma.myapplication.config.ProjectTestRule
import com.alorma.myapplication.config.configureRxThreading
import com.alorma.myapplication.domain.model.Configuration
import com.alorma.myapplication.domain.model.Images
import com.alorma.myapplication.domain.model.Movie
import com.alorma.myapplication.domain.repository.ConfigurationRepository
import com.alorma.myapplication.domain.repository.MoviesRepository
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.mock
import com.schibsted.spain.barista.assertion.BaristaRecyclerViewAssertions.assertRecyclerViewItemCount
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertNotDisplayed
import com.schibsted.spain.barista.interaction.BaristaListInteractions.clickListItem
import io.reactivex.Single
import org.hamcrest.Matcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import java.util.*

class MovieDetailActivityTest {

    companion object {
        const val OVERVIEW: String = "This is an overview"
        const val VOTE: Float = 5.4f
        const val ID: Int = 12
    }

    init {
        configureRxThreading()
    }

    @get:Rule
    val rule = ProjectTestRule(MovieDetailActivity::class.java, this)

    val moviesRepository: MoviesRepository = mock()
    val configRepository: ConfigurationRepository = mock()

    @Before
    fun setup() {
        given(configRepository.getConfig()).willReturn(Single.just(generateConfig()))
        given(moviesRepository.similar(eq(ID))).willReturn(Single.just(emptyList()))
    }

    @Test
    fun onLoadMovie_renderTitleOnScreen() {
        given(moviesRepository.getMovie(eq(ID))).willReturn(Single.just(generateItem(ID)))

        launchWithId()

        assertDisplayed("Title $ID")
    }

    @Test
    fun onLoadMovie_renderOverviewOnScreen() {
        given(moviesRepository.getMovie(eq(ID))).willReturn(Single.just(generateItem(ID)))

        launchWithId()

        assertDisplayed(OVERVIEW)
    }

    @Test
    fun onLoadMovie_renderVotesOnScreen() {
        given(moviesRepository.getMovie(eq(ID))).willReturn(Single.just(generateItem(ID)))

        launchWithId()

        assertDisplayed("5.4")
    }

    @Test
    fun onLoadMovie_renderGenresOnScreen() {
        given(moviesRepository.getMovie(eq(ID))).willReturn(Single.just(generateItem(ID)))

        launchWithId()

        assertDisplayed("Comedy")
        assertDisplayed("Drama")
    }

    @Test
    fun onLoadWithoutSimilarMovies_notDisplaySimilarMoviesLabel() {
        given(moviesRepository.getMovie(eq(ID))).willReturn(Single.just(generateItem(ID)))

        launchWithId()

        assertNotDisplayed(R.id.similarMoviesLabel)
    }

    @Test
    fun onLoadWithSimilarMovies_displaySimilarMoviesLabel() {
        given(moviesRepository.getMovie(eq(ID))).willReturn(Single.just(generateItem(ID)))
        given(moviesRepository.similar(eq(ID))).willReturn(Single.just(generateSimilarItems(30)))

        launchWithId()

        assertDisplayed(R.id.similarMoviesLabel)
    }

    @Test
    fun onLoadWithSimilarMovies_displaySimilarMovies() {
        given(moviesRepository.getMovie(eq(ID))).willReturn(Single.just(generateItem(ID)))
        given(moviesRepository.similar(eq(ID))).willReturn(Single.just(generateSimilarItems(30)))

        launchWithId()

        assertRecyclerViewItemCount(R.id.similarMoviesRecycler, 30)
    }

    @Test
    fun onClickSimilarMovie_displayOtherDetail() {
        Intents.init()

        given(moviesRepository.getMovie(anyInt())).willReturn(Single.just(generateItem(ID)))
        given(moviesRepository.similar(anyInt())).willReturn(Single.just(generateSimilarItems(30)))

        launchWithId()

        clickListItem(R.id.similarMoviesRecycler, 1)

        Intents.intended(getMatcherOpenDetailActivity(), times(2))
        Intents.release()
    }

    private fun launchWithId(id: Int = ID) {
        rule.run(MovieDetailActivity.launch(InstrumentationRegistry.getTargetContext(), id, "Title $id"))
    }

    private fun generateSimilarItems(number: Int): List<Movie> = (1..number).map { generateSimilarItem(it) }

    private fun generateSimilarItem(id: Int): Movie = Movie(id, "Similar $id", OVERVIEW,
            Images("", ""), Date(), VOTE, listOf(1, 2))

    private fun generateItem(id: Int): Movie = Movie(id, "Title $id", OVERVIEW,
            Images("", ""), Date(), VOTE, listOf(1, 2))

    private fun generateConfig(): Configuration = Configuration("url", "500", "500",
            listOf(1 to "Comedy", 2 to "Drama"))

    private fun getMatcherOpenDetailActivity(): Matcher<Intent> = IntentMatchers.hasComponent(MovieDetailActivity::class.java.name)
}