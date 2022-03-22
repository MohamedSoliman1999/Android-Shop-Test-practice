package com.example.androidshoptest.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.example.androidshoptest.data.remote.MockCartRepositoryAndroidTest
import com.example.androidshoptest.ui.addcart.NewCartItemFragment
import com.example.androidshoptest.ui.cartlist.CartFragment
import com.example.androidshoptest.ui.cartlist.CartListViewModel
import com.example.androidshoptest.ui.gallery.GalleryFragment
import javax.inject.Inject

class ShoppingFragmentFactoryAndroidTest @Inject constructor(
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (className) {
            GalleryFragment::class.java.name -> GalleryFragment()
            NewCartItemFragment::class.java.name -> NewCartItemFragment()
            CartFragment::class.java.name -> CartFragment(CartListViewModel(
                MockCartRepositoryAndroidTest()
            ))
            else -> super.instantiate(classLoader, className)
        }

    }
}