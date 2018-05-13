package com.alorma.myapplication.ui.detail

import android.content.Intent
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.intent.Intents
import android.support.test.espresso.intent.VerificationModes.times
import android.support.test.espresso.intent.matcher.IntentMatchers
import com.alorma.myapplication.R
import com.alorma.myapplication.config.ProjectTestRule
import com.alorma.myapplication.config.configureRxThreading
import com.alorma.myapplication.domain.model.Configuration
import com.alorma.myapplication.domain.model.Images
import com.alorma.myapplication.domain.model.TvShow
import com.alorma.myapplication.domain.repository.ConfigurationRepository
import com.alorma.myapplication.domain.repository.ShowsRepository
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

class ShowDetailActivityTest {

    companion object {
        const val OVERVIEW: String = "This is an overview"
        const val VOTE: Float = 5.4f
        const val ID: Int = 12
    }

    init {
        configureRxThreading()
    }

    @get:Rule
    val rule = ProjectTestRule(ShowDetailActivity::class.java, this)

    val showsRepository: ShowsRepository = mock()
    val configRepository: ConfigurationRepository = mock()

    @Before
    fun setup() {
        given(configRepository.getConfig()).willReturn(Single.just(generateConfig()))
        given(showsRepository.similar(eq(ID))).willReturn(Single.just(emptyList()))
    }

    @Test
    fun onLoadShow_renderTitleOnScreen() {
        given(showsRepository.getShow(eq(ID))).willReturn(Single.just(generateItem(ID)))

        launchWithId()

        assertDisplayed("Title $ID")
    }

    @Test
    fun onLoadShow_renderOverviewOnScreen() {
        given(showsRepository.getShow(eq(ID))).willReturn(Single.just(generateItem(ID)))

        launchWithId()

        assertDisplayed(OVERVIEW)
    }

    @Test
    fun onLoadShow_renderVotesOnScreen() {
        given(showsRepository.getShow(eq(ID))).willReturn(Single.just(generateItem(ID)))

        launchWithId()

        assertDisplayed("5.4")
    }

    @Test
    fun onLoadShow_renderGenresOnScreen() {
        given(showsRepository.getShow(eq(ID))).willReturn(Single.just(generateItem(ID)))

        launchWithId()

        assertDisplayed("Comedy")
        assertDisplayed("Drama")
    }

    @Test
    fun onLoadWithoutSimilarShows_notDisplaySimilarShowsLabel() {
        given(showsRepository.getShow(eq(ID))).willReturn(Single.just(generateItem(ID)))

        launchWithId()

        assertNotDisplayed(R.id.similarShowsLabel)
    }

    @Test
    fun onLoadWithSimilarShows_displaySimilarShowsLabel() {
        given(showsRepository.getShow(eq(ID))).willReturn(Single.just(generateItem(ID)))
        given(showsRepository.similar(eq(ID))).willReturn(Single.just(generateSimilarItems(30)))

        launchWithId()

        assertDisplayed(R.id.similarShowsLabel)
    }

    @Test
    fun onLoadWithSimilarShows_displaySimilarShows() {
        given(showsRepository.getShow(eq(ID))).willReturn(Single.just(generateItem(ID)))
        given(showsRepository.similar(eq(ID))).willReturn(Single.just(generateSimilarItems(30)))

        launchWithId()

        assertRecyclerViewItemCount(R.id.similarShowsRecycler, 30)
    }

    @Test
    fun onClickSimilarShow_displayOtherDetail() {
        Intents.init()

        given(showsRepository.getShow(anyInt())).willReturn(Single.just(generateItem(ID)))
        given(showsRepository.similar(anyInt())).willReturn(Single.just(generateSimilarItems(30)))

        launchWithId()

        clickListItem(R.id.similarShowsRecycler, 1)

        Intents.intended(getMatcherOpenDetailActivity(), times(2))
        Intents.release()
    }

    private fun launchWithId(id: Int = ID) {
        rule.run(ShowDetailActivity.launch(InstrumentationRegistry.getTargetContext(), id, "Title $id"))
    }

    private fun generateSimilarItems(number: Int): List<TvShow> = (1..number).map { generateSimilarItem(it) }

    private fun generateSimilarItem(id: Int): TvShow = TvShow(id, "Similar $id", OVERVIEW,
            Images("", ""), Date(), VOTE, listOf(1, 2))

    private fun generateItem(id: Int): TvShow = TvShow(id, "Title $id", OVERVIEW,
            Images("", ""), Date(), VOTE, listOf(1, 2))

    private fun generateConfig(): Configuration = Configuration("url", "500", "500",
            listOf(1 to "Comedy", 2 to "Drama"))

    private fun getMatcherOpenDetailActivity(): Matcher<Intent> = IntentMatchers.hasComponent(ShowDetailActivity::class.java.name)
}