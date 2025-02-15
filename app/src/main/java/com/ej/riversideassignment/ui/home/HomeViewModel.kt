package com.ej.riversideassignment.ui.home

import androidx.annotation.StringRes
import com.ej.riversideassignment.R
import com.ej.riversideassignment.model.TitleDetails
import com.ej.riversideassignment.repositories.TitleRepository
import com.ej.riversideassignment.ui.base.BaseViewModel
import com.ej.riversideassignment.utils.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: TitleRepository,
) : BaseViewModel<HomeIntent>() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Initial)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    override fun onTriggerEvent(eventType: HomeIntent) {
        launch {
            when (eventType) {
                is HomeIntent.SearchByTitle -> searchByTitle(eventType.title)
                is HomeIntent.FavouriteTitle -> updateFavouriteTitle(eventType.titleId,
                    eventType.isFavourite)
            }
        }
    }

    private suspend fun searchByTitle(title: String) {
        if (title.isEmpty()) {
            _uiState.emit(UiState.Failure(R.string.title_empty_error))
        } else {
            _uiState.emit(UiState.Loading)
            try {
                homeRepository.searchByTitle(title.trim()).collect { data ->
                    val uiState = when {
                        data.isEmpty() -> {
                            UiState.Empty
                        }

                        else -> UiState.Success(data)
                    }
                    _uiState.emit(uiState)
                }
            } catch (e: Exception) {
                _uiState.emit(UiState.Failure(R.string.no_internet_connection))
            }
        }
    }

    private suspend fun updateFavouriteTitle(titleId: String, favourite: Boolean) {
        homeRepository.updateFavouriteTitle(titleId, favourite)
    }
}

sealed class UiState {
    data object Initial: UiState()
    data object Empty : UiState()
    data object Loading : UiState()
    data class Success(val data: List<TitleDetails>) : UiState()
    data class Failure(@StringRes val stringRes: Int) : UiState()
}