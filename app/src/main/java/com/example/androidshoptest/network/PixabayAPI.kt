package com.example.androidshoptest.network

import com.example.androidshoptest.BuildConfig
import com.example.androidshoptest.model.datatransfer.ImageResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PixabayAPI {
    @GET("/api/")
    suspend fun searchImage(
        @Query("q") searchQuery:String,
        @Query("key") apiKey: String = BuildConfig.API_KEY
    ):ImageResponse
}