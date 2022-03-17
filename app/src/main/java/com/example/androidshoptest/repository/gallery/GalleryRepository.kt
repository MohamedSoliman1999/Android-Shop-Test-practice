package com.example.androidshoptest.repository.gallery

import com.example.androidshoptest.mainstate.MainState
import com.example.androidshoptest.model.datatransfer.ImageResponse
import kotlinx.coroutines.flow.Flow

interface GalleryRepository {
    suspend fun searchForImage(query: String):ImageResponse// Flow<ImageResponse>
}