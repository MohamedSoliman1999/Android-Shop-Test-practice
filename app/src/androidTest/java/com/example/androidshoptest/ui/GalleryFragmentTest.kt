package com.example.androidshoptest.ui

import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
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
import com.example.androidshoptest.ui.adapter.GalleryAdapter
import com.example.androidshoptest.ui.gallery.GalleryFragment
import com.example.androidshoptest.ui.gallery.GalleryViewModel
import com.example.androidshoptest.util.ResultListenerFragment
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import javax.inject.Inject
import javax.inject.Named

@HiltAndroidTest
@MediumTest
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class GalleryFragmentTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    @Named("test_fragment_factory")
    lateinit var fragmentFactory: ShoppingFragmentFactoryAndroidTest


    //    @Inject
//    @Named("test_db") //need named annotation to tell hilt to where to inject from
//    lateinit var database: AppDatabase
    @Before
    fun setup() {
        hiltRule.inject()
    }

    @After
    fun teardown() {
    }

    @Test
    fun `clickImage_popBackStack`() {
        val navController = Mockito.mock(NavController::class.java)
        val imageUrl = "TESTURL"
        val testViewModel = GalleryViewModel(
            MockGalleryRepositoryAndroidTest(),
            MockCartRepositoryAndroidTest()
        )
        launchFragmentInHiltContainer<GalleryFragment>(
            fragmentFactory = fragmentFactory
        ) {
            Navigation.setViewNavController(requireView(), navController)
            imageAdapter.submitList(listOf(imageUrl))
            galleryViewModel = testViewModel
        }

        Espresso.onView(withId(R.id.rvImages)).perform(
            RecyclerViewActions.actionOnItemAtPosition<GalleryAdapter.GalleryViewHolder>(
                0,
                ViewActions.click()
            )
        )

        Mockito.verify(navController).popBackStack()
        assertThat(testViewModel.selectedImageUrl.getOrAwaitValue()).isEqualTo(imageUrl)
    }
    @Test
    fun testFragmentResultListener() {
        val scenario = launchFragmentInHiltContainer<ResultListenerFragment>(
            fragmentFactory = fragmentFactory
        ){
            val expectedResult = "result"
            parentFragmentManager.setFragmentResult("requestKey", bundleOf("bundleKey" to expectedResult))
            assertThat(result).isEqualTo(expectedResult)
        }
    }
}
