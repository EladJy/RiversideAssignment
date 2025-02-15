package com.ej.riversideassignment.api

import com.ej.riversideassignment.model.TitleDetails
import com.ej.riversideassignment.model.TitleResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitService {
    @GET("/")
    suspend fun searchByTitle(@Query("s") title: String): TitleResponse

    @GET("/")
    suspend fun getTitleDetailsById(@Query("i") titleId: String): TitleDetails
}