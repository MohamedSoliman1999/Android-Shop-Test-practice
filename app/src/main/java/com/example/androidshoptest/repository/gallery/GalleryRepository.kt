package com.example.androidshoptest.repository.gallery

import com.example.androidshoptest.mainstate.MainState
import com.example.androidshoptest.model.datatransfer.ImageResponse
import kotlinx.coroutines.flow.Flow

interface GalleryRepository {
    suspend fun searchForImage(query: String,key:String="21657372-9067a0038327ae13275c76dc1"): Flow<ImageResponse?>
}