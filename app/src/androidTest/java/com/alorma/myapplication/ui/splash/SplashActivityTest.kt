package com.alorma.myapplication.ui.splash

import com.alorma.domain.repository.ConfigurationRepository
import com.alorma.domain.repository.MoviesRepository
import com.alorma.myapplication.R
import com.alorma.myapplication.ui.base.*
import com.nhaarman.mockito_kotlin.mock
import com.schibsted.spain.barista.assertion.BaristaRecyclerViewAssertions.assertRecyclerViewItemCount
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.schibsted.spain.barista.rule.BaristaRule
import org.junit.Rule
import org.junit.Test
import org.koin.dsl.context.ModuleDefinition

class SplashActivityTest : BaseKoinTest() {

    @get:Rule
    val rule = BaristaRule.create(SplashActivity::class.java)

    val moviesRepository: MoviesRepository = mock()
    val configRepository: ConfigurationRepository = mock()

    override fun ModuleDefinition.mockDependencies() {
        factory { moviesRepository }
        factory { configRepository }
    }

    override fun configureMocks() {
        super.configureMocks()

        moviesRepository.asSingleItem()
    }

    @Test
    fun onLoadSplash_configurationRequestSuccess_openMainWithMovies() {
        configRepository.asValidConfig()
        moviesRepository.asListValidData(50)

        rule.launchActivity()

        assertRecyclerViewItemCount(R.id.recycler, 50)
    }

    @Test
    fun onLoadSplash_configurationRequestFail_DataException_openMainWithError_data() {
        configRepository.asDataOriginError()

        rule.launchActivity()

        assertDisplayed(R.string.data_origin_error)
    }

    @Test
    fun onLoadSplash_configurationRequestFail_DataException_openMainWithError_generic() {
        configRepository.asError()

        rule.launchActivity()

        assertDisplayed(R.string.generic_error)
    }
}