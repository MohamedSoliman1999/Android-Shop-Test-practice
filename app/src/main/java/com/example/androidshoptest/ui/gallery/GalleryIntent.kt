package com.example.androidshoptest.ui.gallery

sealed class GalleryIntent<T>(val data: T? = null){
    class SearchImage<T>(data: T? = null) : GalleryIntent<T>(data)
}
