package com.chujunjie.scamwisecampus.data.remote.safebrowsing

import com.chujunjie.scamwisecampus.data.remote.safebrowsing.model.SafeBrowsingResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface SafeBrowsingApi {

    @Headers("Accept: application/json")
    @GET("v5/urls:search")
    suspend fun searchUrl(
        @Query("urls")
        url: String,
        @Query("key")
        apiKey: String
    ): SafeBrowsingResponse
}
