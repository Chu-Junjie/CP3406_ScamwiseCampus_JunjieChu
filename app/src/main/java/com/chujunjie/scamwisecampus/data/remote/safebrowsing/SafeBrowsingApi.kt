package com.chujunjie.scamwisecampus.data.remote.safebrowsing

import com.chujunjie.scamwisecampus.data.remote.safebrowsing.model.SafeBrowsingRequest
import com.chujunjie.scamwisecampus.data.remote.safebrowsing.model.SafeBrowsingResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface SafeBrowsingApi {

    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    @POST("v4/threatMatches:find")
    suspend fun searchUrl(
        @Query("key")
        apiKey: String,

        @Body
        request: SafeBrowsingRequest
    ): SafeBrowsingResponse
}