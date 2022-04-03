package com.example.androidshoptest.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.MediumTest
import app.cash.turbine.test
import com.example.androidshoptest.db.AppDatabase
import com.example.androidshoptest.db.CartDao
import com.example.androidshoptest.getOrAwaitValue
import com.example.androidshoptest.model.entity.CartItem
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named
import kotlin.time.ExperimentalTime

@ExperimentalCoroutinesApi
@MediumTest
@HiltAndroidTest
class ShoppingDaoTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    //This rule tells android test to run everything one by one , without this u will get a error saying cannot complete task
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db") //need named annotation to tell hilt to where to inject from
    lateinit var database: AppDatabase
    private lateinit var dao: CartDao

    @Before
    fun setup() {
        //we use this to inject db reference
        hiltRule.inject()
        /*
        Im keeping the unused comment below as reference to show that we dont need to instatiate the db if we use hilt for testing as well
        below way isnt good if project too big, its okay for sample project like this but not really
         */
        /**
        database = Room.inMemoryDatabaseBuilder( //inMemoryDatabaseBuilder is used to tell the app and room to store data in the RAM
        // rather than persistemce memory so that we can have a new database for each testcases
        ApplicationProvider.getApplicationContext(),
        ShoppingItemDatabase::class.java
        ).allowMainThreadQueries().build() //we use this bcoz testcases should run in single thread ,
        // if they run in background or multiple threads , threads can manipulate each other, we want complete independence
         */
        dao = database.shoppingDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @OptIn(ExperimentalTime::class)
    @Test
    fun insertShoppingItem() = runTest {
        val shoppingItem = CartItem("itemTest", 10, 1F, "url", 1)
        dao.insertShoppingItem(shoppingItem)
        dao.observeAllShoppingItems().test {
            val result=expectItem()
            assertThat(result).contains(shoppingItem)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @OptIn(ExperimentalTime::class)
    @Test
    fun deleteShoppingItem() = runTest {
        val shoppingItem = CartItem("itemTest", 10, 1F, "url", 1)
        dao.insertShoppingItem(shoppingItem)
        dao.deleteShoppingItem(shoppingItem)
        dao.observeAllShoppingItems().test {
            val result=expectItem()
            assertThat(result).doesNotContain(shoppingItem)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @OptIn(ExperimentalTime::class)
    @Test
    fun observeAllShoppingItem() = runTest {
        val shoppingItem = CartItem("itemTest", 10, 1F, "url", 1)
        dao.insertShoppingItem(shoppingItem)
        dao.observeAllShoppingItems().test {
            val result=expectItem()
            assertThat(result).hasSize(1)
            cancelAndIgnoreRemainingEvents()
        }

    }

    @OptIn(ExperimentalTime::class)
    @Test
    fun observeTotalPrice() = runTest {
        val shoppingItem1 = CartItem("itemTest1", 10, 2F, "url", 1)
        val shoppingItem2 = CartItem("itemTest2", 10, 2F, "url", 2)
        val shoppingItem3 = CartItem("itemTest3", 10, 2F, "url", 3)
        dao.insertShoppingItem(shoppingItem1)
        dao.insertShoppingItem(shoppingItem2)
        dao.insertShoppingItem(shoppingItem3)

        dao.observeTotalPrice().test {
            val result=expectItem()
            assertThat(result).isEqualTo(10 * 2 + 10 * 2 + 10 * 2)
            cancelAndIgnoreRemainingEvents()
        }
    }
}