package com.example.androidshoptest.di

import com.example.androidshoptest.data.remote.MockCartRepositoryAndroidTest
import com.example.androidshoptest.data.remote.MockGalleryRepositoryAndroidTest
import com.example.androidshoptest.db.CartDao
import com.example.androidshoptest.network.PixabayAPI
import com.example.androidshoptest.repository.cart.CartRepository
import com.example.androidshoptest.repository.cart.CartRepositoryImpl
import com.example.androidshoptest.repository.gallery.GalleryRepository
import com.example.androidshoptest.repository.gallery.GalleryRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

//@InstallIn(SingletonComponent::class)
//@Module
//object RepositoryModuleTest {
//    @Provides
//    @Singleton
//    fun provideGalleryRepositoryTest(
//    ): GalleryRepository {
//        return MockGalleryRepositoryAndroidTest()
//    }
//    @Provides
//    @Singleton
//    fun provideCartRepositoryTest(
//    ): CartRepository {
//        return MockCartRepositoryAndroidTest()
//    }
//}