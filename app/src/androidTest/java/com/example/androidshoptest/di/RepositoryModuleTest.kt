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
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModuleTest {
    @Provides
//    @Singleton
    @Named("gallery_test_repo")
    fun provideGalleryRepositoryTest(
    ): GalleryRepository {
        return MockGalleryRepositoryAndroidTest()
    }
    @Provides
//    @Singleton
    @Named("cart_test_repo")
    fun provideCartRepositoryTest(
    ): CartRepository {
        return MockCartRepositoryAndroidTest()
    }
}