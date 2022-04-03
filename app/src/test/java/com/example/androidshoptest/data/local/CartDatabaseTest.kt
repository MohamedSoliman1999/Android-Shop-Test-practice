package com.example.androidshoptest.data.local

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.withTransaction
import app.cash.turbine.test
import com.example.androidshoptest.R
import com.example.androidshoptest.db.AppDatabase
import com.example.androidshoptest.db.CartDao
import com.example.androidshoptest.model.entity.CartItem
import com.example.androidshoptest.repository.cart.CartRepositoryImpl
import com.example.androidshoptest.util.TimeUtil
import com.google.common.truth.Truth.assertThat
import io.mockk.*
import io.mockk.impl.annotations.MockK
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.`when`
import kotlin.time.ExperimentalTime


@RunWith(JUnit4::class)
@ExperimentalTime
class CartDatabaseTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var repository: CartRepositoryImpl

    @MockK
    lateinit var context: Context

    @MockK
    lateinit var timeUtil: TimeUtil
    @MockK
    lateinit var database: AppDatabase
    private lateinit var dao: CartDao
    private val catList = listOf(
        CartItem("itemTest1", 10, 2F, "url", 1),
        CartItem("ite*mTest2", 10, 2F, "url", 2),
        CartItem("itemTest3", 10, 2F, "url", 3)
    )

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxed = true)
        mockkStatic(
            "androidx.room.RoomDatabaseKt"
        )
        val transactionLambda = slot<suspend () -> R>()
        coEvery { database.withTransaction(capture(transactionLambda)) } coAnswers {
            transactionLambda.captured.invoke()
        }
        dao = database.shoppingDao()
        repository = CartRepositoryImpl(dao)

    }
    @After
    fun tearDown(){
        database.close()
    }
    @Test
    fun observeToCartITem()=runBlocking{
        every {
            dao.observeAllShoppingItems()
        } answers { flow { emit(catList) } }
        repository.observeAllShoppingItem().test {
            val result = expectItem()
            assertThat(result).isEqualTo(catList)
            expectComplete()
        }

    }

    @Test
    fun observeTotalPrice()=runBlocking{
        every {
            dao.observeTotalPrice()
        } answers { flow { emit(catList.sumByDouble { (it.price*it.amount).toDouble() }.toFloat()) } }
        repository.observeTotalPrice().test {
            val result = expectItem()
            assertThat(result).isEqualTo(60.0f)
            expectComplete()
        }

    }
}