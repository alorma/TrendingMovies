package com.alorma.myapplication.ui.shows

import com.alorma.myapplication.R
import com.alorma.myapplication.config.ProjectTestRule
import com.alorma.myapplication.domain.model.TvShow
import com.alorma.myapplication.domain.repository.ShowsRepository
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.mock
import com.schibsted.spain.barista.assertion.BaristaRecyclerViewAssertions.assertRecyclerViewItemCount
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

    @Test
    fun onLoadItems_showOnScreen() {
        val items = listOf(generateItem(1), generateItem(2))
        given(showsRepository.listAll()).willReturn(Single.just(items))

        rule.run()

        assertRecyclerViewItemCount(R.id.recycler, 2)
        assertDisplayed("Title 1")
        assertDisplayed("Title 2")
    }

    private fun generateItem(id: Int): TvShow = TvShow(id, "Title $id")
}