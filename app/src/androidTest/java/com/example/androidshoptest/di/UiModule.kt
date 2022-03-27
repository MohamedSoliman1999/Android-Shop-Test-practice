package com.example.androidshoptest.di

import android.content.Context
import com.example.androidshoptest.ui.ShoppingFragmentFactoryAndroidTest
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object UiModule {
    @Provides
    @Named("test_fragment_factory")
    fun provideFragmentFactory() =
        ShoppingFragmentFactoryAndroidTest()
}