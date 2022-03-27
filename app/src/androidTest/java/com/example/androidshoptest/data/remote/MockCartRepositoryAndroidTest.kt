package com.example.androidshoptest.data.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.androidshoptest.model.entity.CartItem
import com.example.androidshoptest.repository.cart.CartRepository
import javax.inject.Inject

open class MockCartRepositoryAndroidTest@Inject constructor():CartRepository {
    private val shoppingItems = mutableListOf<CartItem>()
    private val observableShoppingItems = MutableLiveData<List<CartItem>>(shoppingItems)
    private val observableTotalPrice = MutableLiveData<Float>()
    fun setShouldReturnNetworkError(value: Boolean) {
        shouldReturnNetworkError = value
    }

    private var shouldReturnNetworkError = false
    private fun refreshLiveData() {
        observableShoppingItems.postValue(shoppingItems)
        observableTotalPrice.postValue(getTotalPrice())
    }
    private fun getTotalPrice(): Float {
        return shoppingItems.sumByDouble { it.price.toDouble() }.toFloat()
    }
    override suspend fun insertShoppingItem(shoppingItem: CartItem) {
        shoppingItems.add(shoppingItem)
        refreshLiveData()
    }

    override suspend fun deleteShoppingItem(shoppingItem: CartItem) {
        shoppingItems.remove(shoppingItem)
        refreshLiveData()
    }

    override fun observeAllShoppingItem(): LiveData<List<CartItem>> {
        return observableShoppingItems
    }

    override fun observeTotalPrice(): LiveData<Float> {
        return observableTotalPrice
    }
}