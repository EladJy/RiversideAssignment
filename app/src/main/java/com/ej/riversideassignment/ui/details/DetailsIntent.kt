package com.ej.riversideassignment.ui.details

import com.ej.riversideassignment.ui.base.BaseIntent

sealed class DetailsIntent : BaseIntent() {
    data class FetchMovieDetails(val title: String) : DetailsIntent()
    data class FavouriteTitle(val titleId: String, val isFavourite: Boolean): DetailsIntent()
}