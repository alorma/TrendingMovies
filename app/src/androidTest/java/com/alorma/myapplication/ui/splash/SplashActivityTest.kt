package com.alorma.myapplication.ui.splash

import com.alorma.myapplication.R
import com.alorma.myapplication.config.configureRxThreading
import com.alorma.myapplication.domain.exception.DataOriginException
import com.alorma.myapplication.domain.model.Configuration
import com.alorma.myapplication.domain.model.Images
import com.alorma.myapplication.domain.model.Movie
import com.alorma.myapplication.domain.repository.ConfigurationRepository
import com.alorma.myapplication.domain.repository.MoviesRepository
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.mock
import com.schibsted.spain.barista.assertion.BaristaRecyclerViewAssertions.assertRecyclerViewItemCount
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.schibsted.spain.barista.rule.BaristaRule
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext.loadKoinModules
import java.util.*

class SplashActivityTest {

    @get:Rule
    val rule = BaristaRule.create(SplashActivity::class.java)

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

        val items = generateItems(50)
        given(moviesRepository.listAll()).willReturn(Single.just(items))
    }

    @Test
    fun onLoadSplash_configurationRequestSuccess_openMainWithMovies() {
        given(configRepository.getConfig()).willReturn(Single.just(generateConfig()))

        rule.launchActivity()

        assertRecyclerViewItemCount(R.id.recycler, 50)
    }

    @Test
    fun onLoadSplash_configurationRequestFail_DataException_openMainWithError_data() {
        given(configRepository.getConfig()).willReturn(Single.error(DataOriginException()))

        rule.launchActivity()

        assertDisplayed(R.string.data_origin_error)
    }

    @Test
    fun onLoadSplash_configurationRequestFail_DataException_openMainWithError_generic() {
        given(configRepository.getConfig()).willReturn(Single.error(Exception()))

        rule.launchActivity()

        assertDisplayed(R.string.generic_error)
    }

    private fun generateItems(number: Int): List<Movie> = (1..number).map { generateItem(it) }

    private fun generateItem(id: Int): Movie = Movie(id, "Title $id", "", Images("", ""), Date(), 0f, listOf())

    private fun generateConfig(): Configuration = Configuration("url", "500", "500",
            listOf(1 to "Comedy", 2 to "Drama"))
}