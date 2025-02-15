package com.ej.riversideassignment.model

import com.google.gson.annotations.SerializedName

data class TitleResponse(
    @SerializedName("Search") val list: List<TitleDetails>?,
    @SerializedName("totalResults") val totalResults: String?,
    @SerializedName("Response") val response: String?
)