package com.alorma.myapplication.ui.shows

import com.alorma.myapplication.R
import com.alorma.myapplication.config.ProjectTestRule
import com.alorma.myapplication.domain.repository.ShowsRepository
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.mock
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import io.reactivex.Single
import org.junit.Rule
import org.junit.Test

class ShowsActivityTest {
    @get:Rule
    val rule = ProjectTestRule(ShowsActivity::class.java, this)

    val showsRepository: ShowsRepository = mock()

    @Test
    fun onLoadError_showErrorOnScreen() {
        given(showsRepository.listAll()).willReturn(Single.error(Exception()))

        rule.run()

        assertDisplayed(R.string.generic_error)
    }
}