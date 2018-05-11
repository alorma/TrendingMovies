package com.alorma.myapplication.ui.shows

import com.alorma.myapplication.R
import com.alorma.myapplication.config.ProjectTestRule
import com.alorma.myapplication.domain.model.TvShow
import com.alorma.myapplication.domain.repository.ShowsRepository
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.mock
import com.schibsted.spain.barista.assertion.BaristaRecyclerViewAssertions.assertRecyclerViewItemCount
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.schibsted.spain.barista.interaction.BaristaListInteractions.scrollListToPosition
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
        val items = generateItems(50)
        given(showsRepository.listAll()).willReturn(Single.just(items))

        rule.run()

        assertRecyclerViewItemCount(R.id.recycler, 50)
        assertDisplayed("Title 1")
        assertDisplayed("Title 2")
    }

    @Test
    fun onLoadManyItems_onScroll_loadMore() {
        val items = generateItems(50)
        given(showsRepository.listAll()).willReturn(Single.just(items))
        val itemsPage = generateItems(25)
        given(showsRepository.listNextPage()).willReturn(Single.just(itemsPage))

        rule.run()

        assertRecyclerViewItemCount(R.id.recycler, 50)

        scrollListToPosition(R.id.recycler, 49)

        assertRecyclerViewItemCount(R.id.recycler, 75)
    }

    private fun generateItems(number: Int): List<TvShow> = (1..number).map { generateItem(it) }

    private fun generateItem(id: Int): TvShow = TvShow(id, "Title $id")
}