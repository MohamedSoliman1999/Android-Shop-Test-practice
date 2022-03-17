package com.example.androidshoptest.repository.gallery

import android.content.Context
import com.example.androidshoptest.mainstate.MainState
import com.example.androidshoptest.model.datatransfer.ImageResponse
import com.example.androidshoptest.network.PixabayAPI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject

class GalleryRepositoryImpl@Inject constructor(
//    private val context: Context,
    private val pixabayAPI: PixabayAPI
):GalleryRepository {
    override suspend fun searchForImage(query: String)=pixabayAPI.searchImage(query) /*flow {
        emit(pixabayAPI.searchImage(query))
    }*/



//    Flow<ImageResponse> {
//        return try {
//            val response = pixabayAPI.searchImage(query)
//            if (response.isSuccessful){
//                response.body()?.let {imageResponse ->
//                    return@let MainState.Success(imageResponse)
//                }?: MainState.Error(Throwable("An unknown error occurred"))
//            }else{
//                MainState.Error(Throwable("An unknown error occurred"))
//            }
//        }catch (e: Exception){
//            return MainState.Error(e)
//        }
//    }
}