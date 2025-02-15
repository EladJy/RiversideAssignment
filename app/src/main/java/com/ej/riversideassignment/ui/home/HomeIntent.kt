package com.ej.riversideassignment.ui.home

import com.ej.riversideassignment.ui.base.BaseIntent

sealed class HomeIntent : BaseIntent() {
    data class SearchByTitle(val title: String) : HomeIntent()
    data class FavouriteTitle(val titleId: String, val isFavourite: Boolean): HomeIntent()
}