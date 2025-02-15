package com.ej.riversideassignment.api

import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class ApiKeyQueryInterceptor(private val apiKey: String) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest: Request = chain.request()
        val originalHttpUrl: HttpUrl = originalRequest.url

        val newUrl = originalHttpUrl.newBuilder()
            .addQueryParameter("apikey", apiKey)
            .build()

        val requestWithApiKey = originalRequest.newBuilder()
            .url(newUrl)
            .build()

        return chain.proceed(requestWithApiKey)
    }
}