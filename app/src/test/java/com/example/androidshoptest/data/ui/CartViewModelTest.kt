package com.example.androidshoptest.data.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.example.androidshoptest.MainCoroutineRule
import com.example.androidshoptest.model.entity.CartItem
import com.example.androidshoptest.repository.cart.CartRepositoryImpl
import com.example.androidshoptest.ui.cartlist.CartListViewModel
import com.google.common.truth.Truth
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.time.ExperimentalTime

@RunWith(JUnit4::class)
@ExperimentalTime
class CartViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    val rule = MainCoroutineRule()
    @MockK
    lateinit var cartRpo:CartRepositoryImpl
    lateinit var cartViewModel:CartListViewModel
    private val catList = listOf(
        CartItem("itemTest1", 10, 2F, "url", 1),
        CartItem("ite*mTest2", 10, 2F, "url", 2),
        CartItem("itemTest3", 10, 2F, "url", 3)
    )
    @Before
    fun setup(){
        MockKAnnotations.init(this, relaxed = true)
        cartViewModel= CartListViewModel(cartRepo = cartRpo)
    }
    @Test
    fun observeToCartITem()= runBlocking{
        coEvery {
            cartRpo.observeAllShoppingItem()
        } answers { flow { emit(catList) } }
        cartViewModel.cartItems.collectLatest {
            coVerify {cartRpo.observeAllShoppingItem()}
            Truth.assertThat(it).isEqualTo(catList)
        }

    }

    @Test
    fun observeTotalPrice()= runBlocking{
        coEvery {
            cartRpo.observeTotalPrice()
        } answers { flow { emit(catList.sumByDouble { (it.price*it.amount).toDouble() }.toFloat()) } }
        cartViewModel.cartTotalPrice.collectLatest {
//            val result = expectItem()
            Truth.assertThat(it).isEqualTo(60.0f)
//            expectComplete()
        }

    }
}