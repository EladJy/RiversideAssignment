package com.ej.riversideassignment.ui.home

import com.CoroutineTestRule
import com.ej.riversideassignment.R
import com.ej.riversideassignment.model.TitleDetails
import com.ej.riversideassignment.repositories.TitleRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Mock
    private lateinit var titleRepository: TitleRepository

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        viewModel = HomeViewModel(titleRepository)
    }

    @Test
    fun `empty title - search by title - error to enter a title`() = runTest {
        viewModel.onTriggerEvent(HomeIntent.SearchByTitle(""))

        val uiState = viewModel.uiState.first()

        assertTrue(uiState is UiState.Failure)
        assertEquals(R.string.title_empty_error, (uiState as UiState.Failure).stringRes)
    }

    @Test
    fun `title - search by title returns empty - show empty with error`() = runTest {
        val title = "Some Title"
        whenever(titleRepository.searchByTitle(title)).thenReturn(flow { emit(emptyList()) })

        viewModel.onTriggerEvent(HomeIntent.SearchByTitle(title))

        val uiState = viewModel.uiState.first()

        assertTrue(uiState is UiState.Empty)
    }

    @Test
    fun `title - search by title - show results`() = runTest {
        val title = "Some Title"
        val titleDetails = TitleDetails(imdbID = "1", title = title, year = "1995", posterUrl = "")

        whenever(titleRepository.searchByTitle(title)).thenReturn(flow { emit(listOf(titleDetails)) })

        viewModel.onTriggerEvent(HomeIntent.SearchByTitle(title))

        val uiState = viewModel.uiState.first()

        assertTrue(uiState is UiState.Success)
        assertEquals(1, (uiState as UiState.Success).data.size)
        assertEquals(title, uiState.data[0].title)
    }

    @Test
    fun `title - trigger add to favourite - update favourite`() = runTest {
        val titleId = "1"
        val isFavourite = true

        viewModel.onTriggerEvent(HomeIntent.FavouriteTitle(titleId, isFavourite))

        verify(titleRepository).updateFavouriteTitle(titleId, isFavourite)
    }
}
