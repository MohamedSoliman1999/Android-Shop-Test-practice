package com.example.androidshoptest.ui


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.example.androidshoptest.R
import com.example.androidshoptest.data.remote.MockCartRepositoryAndroidTest
import com.example.androidshoptest.data.remote.MockGalleryRepositoryAndroidTest
import com.example.androidshoptest.getOrAwaitValue
import com.example.androidshoptest.launchFragmentInHiltContainer
import com.example.androidshoptest.model.entity.CartItem
import com.example.androidshoptest.ui.addcart.NewCartItemFragment
import com.example.androidshoptest.ui.addcart.NewCartItemFragmentDirections
import com.example.androidshoptest.ui.gallery.GalleryViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*
import com.google.common.truth.Truth.assertThat
import org.mockito.Mockito
import javax.inject.Inject
import javax.inject.Named


@HiltAndroidTest
@MediumTest
@ExperimentalCoroutinesApi
class NewCartItemFragmentTest {
    @get:Rule
    val hiltAndroidRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_fragment_factory")
    lateinit var fragmentFactory: ShoppingFragmentFactoryAndroidTest
    @Inject
    lateinit var mockGalleryRepositoryAndroidTest:MockGalleryRepositoryAndroidTest
    @Inject
    lateinit var mockCartRepositoryAndroidTest: MockCartRepositoryAndroidTest
    lateinit var testViewModel: GalleryViewModel
    @Before
    fun setup() {
        hiltAndroidRule.inject()
        testViewModel = GalleryViewModel(
            mockGalleryRepositoryAndroidTest,
            mockCartRepositoryAndroidTest
        )
    }

    @Test
    fun clickInsertIntoDb_itemInsertedIntoDb() {
        launchFragmentInHiltContainer<NewCartItemFragment>(
            fragmentFactory = fragmentFactory
        ) {
            galleryViewModel = testViewModel
        }
        onView(withId(R.id.etShoppingItemName))
            .perform(ViewActions.replaceText("shopping item"))
        onView(withId(R.id.etShoppingItemAmount)).perform(ViewActions.replaceText("5"))
        onView(withId(R.id.etShoppingItemPrice)).perform(ViewActions.replaceText("5.5"))
        onView(withId(R.id.btnAddShoppingItem)).perform(ViewActions.click())

        assertThat(testViewModel.getShoppingItems.getOrAwaitValue())
            .contains(CartItem("shopping item", 5, 5.5f, ""))
    }

    @Test
    fun pressBackButton_imageUrlSetToEmptyString() {
        val navController = Mockito.mock(NavController::class.java)
        launchFragmentInHiltContainer<NewCartItemFragment>(
            fragmentFactory = fragmentFactory
        ) {
            Navigation.setViewNavController(requireView(), navController)
            galleryViewModel = testViewModel
            testViewModel.selectedImageUrl.postValue("http://url")
        }
        navController.popBackStack()
        val value = testViewModel.selectedImageUrl.getOrAwaitValue()
        assertThat(value).isNotEmpty()
    }


    @Test
    fun clickAddImageBtn_navigateToImagePicker() {
        val navController = Mockito.mock(NavController::class.java)

        launchFragmentInHiltContainer<NewCartItemFragment>(
            fragmentFactory = fragmentFactory
        ) {
            Navigation.setViewNavController(requireView(), navController)
        }

        onView(withId(R.id.ivShoppingImage)).perform(ViewActions.click())

        Mockito.verify(navController).navigate(
            NewCartItemFragmentDirections.actionNewCartItemFragmentToGalleryFragment()
        )
    }


    @Test
    fun pressBackButton_popBackStack(){
        val navController = Mockito.mock(NavController::class.java)
        launchFragmentInHiltContainer<NewCartItemFragment> (
            fragmentFactory = fragmentFactory
        ) {
            Navigation.setViewNavController(requireView(),navController)
        }
        navController.popBackStack()
        Mockito.verify(navController).popBackStack()
    }
}