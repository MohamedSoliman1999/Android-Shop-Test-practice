package com.example.androidshoptest.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.example.androidshoptest.R
import com.example.androidshoptest.data.remote.MockCartRepositoryAndroidTest
import com.example.androidshoptest.data.remote.MockGalleryRepositoryAndroidTest
import com.example.androidshoptest.getOrAwaitValue
import com.example.androidshoptest.launchFragmentInHiltContainer
import com.example.androidshoptest.model.entity.CartItem
import com.example.androidshoptest.ui.adapter.CartItemAdapter
import com.example.androidshoptest.ui.cartlist.CartFragment
import com.example.androidshoptest.ui.cartlist.CartFragmentDirections
import com.example.androidshoptest.ui.cartlist.CartListViewModel
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import javax.inject.Inject
import javax.inject.Named

@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class CartListFragmentTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_fragment_factory")
    lateinit var testFragmentFactoryAndroidTest: ShoppingFragmentFactoryAndroidTest

    @Before
    fun setup(){
        hiltRule.inject()
    }

    @Test
    fun swipeShoppingItem_deleteItemInDb(){
        var testViewModel= CartListViewModel(MockCartRepositoryAndroidTest())
        val shoppingItem = CartItem("test",1,1f,"testurl",1)
        launchFragmentInHiltContainer<CartFragment>(
            fragmentFactory = testFragmentFactoryAndroidTest
        ) {
            _cartListViewModel=testViewModel
            testViewModel.insertCartItem(shoppingItem)
            cartAdapter.submitList(listOf(shoppingItem))
        }
        Espresso.onView(withId(R.id.rvShoppingItems)).perform(
            RecyclerViewActions.actionOnItemAtPosition<CartItemAdapter.CartViewHolder>(
                0,
                ViewActions.swipeLeft()
            )
        )
        assertThat(testViewModel.cartItems.getOrAwaitValue()).isEmpty()
    }

    @Test
    fun clickAddItemBtn_NavigateToAddShoppingFrag(){
        val findVanController = Mockito.mock(NavController::class.java)

        launchFragmentInHiltContainer<CartFragment> (
            fragmentFactory = testFragmentFactoryAndroidTest
        ) {
            Navigation.setViewNavController(requireView(),findVanController)
        }

        Espresso.onView(withId(R.id.fabAddShoppingItem)).perform(ViewActions.click())

        Mockito.verify(findVanController).navigate(
            CartFragmentDirections.actionCartFragmentToNewCartItemFragment()
        )
    }
}