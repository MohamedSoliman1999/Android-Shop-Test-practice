package com.example.androidshoptest.network

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.androidshoptest.di.DatabaseModuleTesting
import com.example.androidshoptest.di.RepositoryModule
import com.example.androidshoptest.di.RepositoryModuleTest
import com.example.androidshoptest.di.UiModule
import com.example.androidshoptest.repository.gallery.GalleryRepositoryImpl
import com.example.androidshoptest.util.Constants
import com.google.common.truth.Truth.assertThat
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import junit.framework.TestCase
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Inject

@MediumTest
@RunWith(AndroidJUnit4ClassRunner::class)
@HiltAndroidTest
@UninstallModules(RepositoryModuleTest::class,DatabaseModuleTesting::class,UiModule::class)
class GalleryRepositoryTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    var hiltRule = HiltAndroidRule(this)
    @Inject
    lateinit var api:PixabayAPI
    lateinit var repo: GalleryRepositoryImpl

    @Before
    public override fun setUp() {
        super.setUp()
        hiltRule.inject()
        repo = GalleryRepositoryImpl(ApplicationProvider.getApplicationContext(),api)
    }

    @Test
    fun testGalleryApi() = runBlocking {
        repo.searchForImage("v", "21657372-9067a0038327ae13275c76dc1").collectLatest {
            assertThat(it.hits.size).isEqualTo(20)
        }
    }

}