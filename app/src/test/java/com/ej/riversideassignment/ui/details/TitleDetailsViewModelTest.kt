package com.ej.riversideassignment.ui.details

import com.CoroutineTestRule
import com.ej.riversideassignment.model.TitleDetails
import com.ej.riversideassignment.repositories.TitleRepository
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import com.ej.riversideassignment.R

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class TitleDetailsViewModelTest {

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Mock
    private lateinit var titleRepository: TitleRepository
    private lateinit var viewModel: TitleDetailsViewModel

    @Before
    fun setup() {
        viewModel = TitleDetailsViewModel(titleRepository)
    }

    @Test
    fun `title - fetch details - success to get title details`() = runTest {
        val titleId = "movie_123"
        val expectedDetails =
            TitleDetails(imdbID = "movie_123", title = "Movie Title", year = "1995", posterUrl = "")

        whenever(titleRepository.getTitleDetailsById(titleId)).thenReturn(flow {
            emit(expectedDetails)
        })
        viewModel.onTriggerEvent(DetailsIntent.FetchMovieDetails(titleId))

        assertTrue(viewModel.uiState.value is UiState.Success)
        val successState = viewModel.uiState.value as UiState.Success
        assertEquals(expectedDetails, successState.data)
    }

    @Test
    fun `title - fetch details - failed to get title details`() = runTest {
        val titleId = "movie_123"

        whenever(titleRepository.getTitleDetailsById(titleId)).thenThrow(RuntimeException("Network Error"))

        viewModel.onTriggerEvent(DetailsIntent.FetchMovieDetails(titleId))

        assertTrue(viewModel.uiState.value is UiState.Failure)
        val failureState = viewModel.uiState.value as UiState.Failure
        assertEquals(R.string.failed_to_load_movie_details, failureState.stringRes)
    }

    @Test
    fun `title - trigger add to favourite - update favourite`() = runTest {
        val titleId = "movie_123"
        val isFavourite = true

        viewModel.onTriggerEvent(DetailsIntent.FavouriteTitle(titleId, isFavourite))

        verify(titleRepository).updateFavouriteTitle(titleId, isFavourite)
    }
}