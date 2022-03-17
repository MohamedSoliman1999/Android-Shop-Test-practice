package com.example.androidshoptest.model.datatransfer

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
//@JsonClass(generateAdapter = true)
data class ImageResponse(
    @field:Json(name = "hits")
    val hits: List<ImageResult>,
    @field:Json(name = "total")
    val total: Int,
    @field:Json(name = "totalHits")
    val totalHits: Int
) : Parcelable