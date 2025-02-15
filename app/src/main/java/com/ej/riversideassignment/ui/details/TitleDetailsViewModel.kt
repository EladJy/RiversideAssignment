package com.ej.riversideassignment.ui.details

import androidx.annotation.StringRes
import com.ej.riversideassignment.R
import com.ej.riversideassignment.model.TitleDetails
import com.ej.riversideassignment.repositories.TitleRepository
import com.ej.riversideassignment.ui.base.BaseViewModel
import com.ej.riversideassignment.utils.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class TitleDetailsViewModel @Inject constructor(private val titleRepository: TitleRepository) :
    BaseViewModel<DetailsIntent>() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<Event>()
    val events: SharedFlow<Event> = _events.asSharedFlow()

    override fun onTriggerEvent(eventType: DetailsIntent) {
        launch {
            when (eventType) {
                is DetailsIntent.FetchMovieDetails -> fetchMovieDetails(eventType.title)
                is DetailsIntent.FavouriteTitle -> updateFavouriteTitle(eventType.titleId,
                    eventType.isFavourite)
            }
        }
    }

    private suspend fun fetchMovieDetails(titleId: String) {
        _uiState.emit(UiState.Loading)
        try {
            titleRepository.getTitleDetailsById(titleId).collect { itemDetails ->
                if (itemDetails != null) {
                    if (itemDetails.plot == null) {
                        _events.emit(Event.ShowSnackbar(R.string.part_data))
                    }
                    _uiState.emit(UiState.Success(itemDetails))
                } else {
                    emitFailure(R.string.failed_to_load_movie_details)
                }
            }
        } catch (e: RuntimeException) {
            emitFailure(R.string.failed_to_load_movie_details)
        }
    }

    private suspend fun emitFailure(@StringRes stringRes: Int) {
        _uiState.emit(UiState.Failure(stringRes))
    }

    private suspend fun updateFavouriteTitle(titleId: String, favourite: Boolean) {
        titleRepository.updateFavouriteTitle(titleId, favourite)
    }
}

sealed class UiState {
    data object Loading : UiState()
    data class Success(val data: TitleDetails) : UiState()
    data class Failure(@StringRes val stringRes: Int) : UiState()
}

sealed interface Event {
    data class ShowSnackbar(@StringRes val stringRes: Int) : Event
}