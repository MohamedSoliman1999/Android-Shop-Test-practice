package com.example.androidshoptest.vm

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.Fragment
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import app.cash.turbine.test
import com.example.androidshoptest.getOrAwaitValue
import com.example.androidshoptest.mainstate.MainState
import com.example.androidshoptest.model.entity.CartItem
import com.example.androidshoptest.repository.cart.CartRepository
import com.example.androidshoptest.repository.gallery.GalleryRepository
import com.example.androidshoptest.ui.gallery.GalleryIntent
import com.example.androidshoptest.ui.gallery.GalleryViewModel
import com.example.androidshoptest.util.Events
import com.example.androidshoptest.util.TimeUtil
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.takeWhile
import org.junit.*
import org.junit.runner.RunWith
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@MediumTest
@RunWith(AndroidJUnit4ClassRunner::class)
@HiltAndroidTest
@OptIn(ExperimentalTime::class)
class RealGalleryViewModelTest : CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Unconfined

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var galleryRepo: GalleryRepository

    @Inject
    lateinit var cartRpo: CartRepository

    lateinit var galleryViewModel: GalleryViewModel

    @Before
    fun setUp() {
        hiltRule.inject()
        galleryViewModel = GalleryViewModel(galleryRepo, cartRpo)
    }

    @After
    fun tearDown() {

    }

    @Test//(timeout = 5000)
    fun testGalleryApiWithJob() = runBlocking {
            val job = launch {
                galleryViewModel.imageResponse.collectLatest {
                    delay(5000)//to skip loading status
                    println("TEST___ $it  Loading:${it is MainState.Loading}   Success:${it is MainState.Success}")
                    assertThat(it.data!!.hits.size).isEqualTo(20)
                }
            }
        delay(6000)//to skip loading status
        job.cancel()
        galleryViewModel.galleryIntent.send(GalleryIntent.SearchImage("v"))
    }

    @Test
    fun galleryRepoApiTest() = runBlocking {
        galleryRepo.searchForImage("v", "21657372-9067a0038327ae13275c76dc1").collectLatest {
            assertThat(it!!.hits.size).isEqualTo(20)
        }
    }

    @Test
    fun addition_isCorrect() {
        Assert.assertEquals(4, 2 + 2)
    }
    //    var timeUtil: TimeUtil=TimeUtil()
    @Test//(timeout = 10000)
    fun testGalleryApiUsingFilter() = runBlocking {
        galleryViewModel.galleryIntent.send(GalleryIntent.SearchImage("v"))
        galleryViewModel.imageResponse.filter {
            it is MainState.Success
        }.test(timeout =Duration.seconds(2) ) {
            val result = expectItem()
            println("TEST___ $result  Loading:${result is MainState.Loading}   Success:${result is MainState.Success}")
            assertThat(result.data!!.hits.size).isEqualTo(20)
//            expectComplete()
            cancelAndIgnoreRemainingEvents()
        }
    }

}