package com.example.androidshoptest.data.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asFlow
import app.cash.turbine.test
import com.example.androidshoptest.MainCoroutineRule
import com.example.androidshoptest.mainstate.MainState
import com.example.androidshoptest.model.datatransfer.ImageResponse
import com.example.androidshoptest.model.datatransfer.ImageResult
import com.example.androidshoptest.model.entity.CartItem
import com.example.androidshoptest.repository.cart.CartRepositoryImpl
import com.example.androidshoptest.repository.gallery.GalleryRepository
import com.example.androidshoptest.repository.gallery.GalleryRepositoryImpl
import com.example.androidshoptest.ui.gallery.GalleryIntent
import com.example.androidshoptest.ui.gallery.GalleryViewModel
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.*
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@RunWith(JUnit4::class)
@ExperimentalTime
@OptIn(ExperimentalCoroutinesApi::class)
class GalleryViewModelTest: CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Unconfined

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val rule = MainCoroutineRule()

    @MockK
    lateinit var cartRepo: CartRepositoryImpl

    @MockK
    lateinit var galleryRepo: GalleryRepositoryImpl
    lateinit var galleryViewModel: GalleryViewModel
    private val catList:ArrayList<CartItem> = arrayListOf(
        CartItem("itemTest1", 10, 2F, "url", 1),
        CartItem("ite*mTest2", 10, 2F, "url", 2),
        CartItem("itemTest3", 10, 2F, "url", 3)
    )

    @MockK
    lateinit var imageResult: ImageResult
    private lateinit var galleryResponse: ImageResponse

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        var imageResultList = listOf(imageResult)
        galleryResponse = ImageResponse(imageResultList, 10, 2)
        coEvery { galleryRepo.searchForImage(any()) } answers {
            flow {
                emit(galleryResponse)
            }
        }
        coEvery { cartRepo.observeAllShoppingItem() } answers {
            flow {
                emit(catList)
            }
        }
        coEvery { cartRepo.observeTotalPrice() } answers {
            flow {
                emit(catList.sumByDouble { (it.amount * it.price).toDouble() }.toFloat())
            }
        }
        coEvery{cartRepo.insertShoppingItem(any())}.answers {
            catList.add(CartItem("Name", 5, 100f,""))
        }
        galleryViewModel = GalleryViewModel(galleryRepo, cartRepo)
    }

    @After
    fun tearDown() {
    }

    @Test//(timeout = 5000)
    fun getGalleryResponseFromApi() = runBlocking {
        galleryViewModel.galleryIntent.send(GalleryIntent.SearchImage("v"))
        galleryViewModel.imageResponse.test() {
            val result = expectItem()
            assertThat(result).isInstanceOf(MainState.Success::class.java)
            assertThat(result.data).isEqualTo(galleryResponse)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun validateShoppingItemTest() = runBlocking {
        galleryViewModel.validateShoppingItem("Name", "5", "")
        galleryViewModel.insertShoppingItem.asFlow().test {
            val result = expectItem()
            assertThat(result.peekContent()).isInstanceOf(MainState.Error::class.java)
            assertThat(result.peekContent().throwable!!.message).isEqualTo("Please enter all information")
            cancelAndIgnoreRemainingEvents()
        }
    }
    @Test
    fun insertItemToDBTest()=runBlocking{
        galleryViewModel.validateShoppingItem("Name", "5", "100")
        galleryViewModel.insertShoppingItem.asFlow().test {
            val result = expectItem()
            assertThat(result.peekContent()).isInstanceOf(MainState.Success::class.java)
            cancelAndIgnoreRemainingEvents()
        }
    }
}