package com.example.androidshoptest.data.remote

import com.example.androidshoptest.mainstate.MainState
import com.example.androidshoptest.model.datatransfer.ImageResponse
import com.example.androidshoptest.repository.gallery.GalleryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

open class MockGalleryRepositoryAndroidTest@Inject constructor(): GalleryRepository {

    private var shouldReturnNetworkError = false
    override suspend fun searchForImage(query: String,key:String):Flow<ImageResponse?> = flow {
        val value= if(shouldReturnNetworkError) {
            null
        } else {
            ImageResponse(listOf(), 0, 0)
        }
        emit(value)
    }
}