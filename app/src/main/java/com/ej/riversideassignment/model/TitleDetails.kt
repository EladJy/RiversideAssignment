package com.ej.riversideassignment.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(indices = [Index(value = ["title"])])
data class TitleDetails(
    @PrimaryKey @SerializedName("imdbID") val imdbID: String,
    @SerializedName("Title") val title: String,
    @SerializedName("Year") val year: String,
    @SerializedName("Plot") val plot: String? = null,
    @SerializedName("Actors") val actors: String? = null,
    @SerializedName("isFavourite") val isFavourite: Boolean = false,
    @SerializedName("Poster") val posterUrl: String,
)