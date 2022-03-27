package com.example.androidshoptest.network

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.androidshoptest.repository.gallery.GalleryRepositoryImpl
import com.example.androidshoptest.util.Constants
import com.google.common.truth.Truth.assertThat
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
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

@MediumTest
@RunWith(AndroidJUnit4::class)
class GalleryRepositoryTest : TestCase() {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var repo: GalleryRepositoryImpl

    @Before
    public override fun setUp() {
        super.setUp()
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()
        val api = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(PixabayAPI::class.java)
        repo = GalleryRepositoryImpl(api)
    }

    @Test
    fun testGalleryApi() = runBlocking {
        repo.searchForImageTest("v", "21657372-9067a0038327ae13275c76dc1").collectLatest {
            assertThat(it.hits.size).isEqualTo(20)
        }
    }

}