package com.example.androidshoptest.di

import android.content.Context
import com.example.androidshoptest.db.CartDao
import com.example.androidshoptest.network.PixabayAPI
import com.example.androidshoptest.repository.cart.CartRepository
import com.example.androidshoptest.repository.cart.CartRepositoryImpl
import com.example.androidshoptest.repository.gallery.GalleryRepository
import com.example.androidshoptest.repository.gallery.GalleryRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {
    @Provides
    @Singleton
    fun provideGalleryRepository(
        @ApplicationContext appContext: Context,
        pixabayAPI:PixabayAPI
    ): GalleryRepository {
        return GalleryRepositoryImpl(appContext, pixabayAPI = pixabayAPI)
    }
    @Provides
    @Singleton
    fun provideCartRepository(
//        @ApplicationContext appContext: Context,
        cartDao: CartDao
    ): CartRepository {
        return CartRepositoryImpl(/*appContext,*/ shoppingDao = cartDao)
    }
}