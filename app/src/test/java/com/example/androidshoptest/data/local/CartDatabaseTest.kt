package com.example.androidshoptest.data.local

import android.content.Context
import androidx.room.withTransaction
import com.example.androidshoptest.R
import com.example.androidshoptest.db.AppDatabase
import com.example.androidshoptest.db.CartDao
import com.example.androidshoptest.model.entity.CartItem
import com.example.androidshoptest.repository.cart.CartRepositoryImpl
import com.example.androidshoptest.util.TimeUtil
import io.mockk.*
import io.mockk.impl.annotations.MockK
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.flow.flow
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.`when`
import kotlin.time.ExperimentalTime


@RunWith(JUnit4::class)
@ExperimentalTime
class CartDatabaseTest {
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
        CartItem("itemTest2", 10, 2F, "url", 2),
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
//    @Test
//    fun observeTotalPrice(){
//        every {
//            dao.observeAllShoppingItems(any())
//        } answers { flow { emit(catList) } }
//    }

}